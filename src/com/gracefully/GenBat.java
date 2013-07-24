/**
 * 
 */
package com.gracefully;

import java.io.File;
import java.util.ResourceBundle;

import com.gracefully.util.FileUtils;
import com.gracefully.util.StringValueUtils;

/**
 * @author Canni
 *
 *是否服务器
 *需要考的文件
 */
public class GenBat {

	private static String sfn = null;
	private static String pd = null;
	private static String lp = null;
	private static String jp = null;
//	private static String sep = null;
	private static String mc = null;
	private static String ac = null;
	private static boolean isServerService = true;
	private static int system = 1;
	private static String param = null;
	private static final int WINDOWS = 1;
	private static final int LINUX = 2;
	private static final int WINDOWS_WILD = 3;
	private static final int LINUX_WILD = 4;
//	private static final String wrap = "\r\n";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ResourceBundle rb = ResourceBundle.getBundle("exportFounderML");
//		sfn = ConfigManager.getConfigProperty("scriptfilename");
//		pd = ConfigManager.getConfigProperty("project-dir");
//		lp = ConfigManager.getConfigProperty("lib-path");
//		jp = ConfigManager.getConfigProperty("jre-path");
//		mc = ConfigManager.getConfigProperty("mainClass");
//		system = StringValueUtils.getInt(ConfigManager.getConfigProperty("system"));
		sfn = rb.getString("scriptfilename");
		pd = rb.getString("project-dir");
		lp = rb.getString("lib-path");
		jp = rb.getString("jre-path");
		mc = rb.getString("mainClass");
//		system = StringValueUtils.getInt(rb.getString("system"));
		param = rb.getString("JVMParam");
		isServerService = StringValueUtils.getBoolean(rb.getString("isServerService"));
		ac = rb.getString("addition_classpath");
		
		valid(pd);
		
		GenBat gb = new GenBat();
//		setSystem(WINDOWS);
		setSystem(WINDOWS_WILD);
		createBatch(gb);
//		setSystem(LINUX);
		setSystem(WINDOWS_WILD);
		createBatch(gb);
	}
	
	private static void setSystem(int system1) {
		system = system1;
	}

	private static void createBatch(GenBat gb) {
		BatchCreator ec = getBatchCreator(system, gb);
		ec.createEnv();
		ec.createRun();
	}

	private static BatchCreator getBatchCreator(int system,GenBat gb) {
		return gb.new BatchCreator(system,gb) {
			BatchCreator bc = getCreator();
			private BatchCreator getCreator() {
				if(bc == null){
					if(system==WINDOWS){
						bc = gb.new WindowsBatchCreator(system,gb);
					}else{
						bc = gb.new LinuxBatchCreator(system,gb);
					}
				}
				return bc;
			}

			boolean createEnv() {
				return bc.createEnv();
			}

			boolean createRun() {
				return bc.createRun();
			}
		};
	}

	private static void valid(String dir){
		File file = new File(dir);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	public abstract class BatchCreator{
		int system = 0;
		GenBat gb = null;
		public BatchCreator(int system, GenBat gb){
			this.system = system;
			this.gb = gb;
		}
		/**
		 * create setEnv.cmd|setEnv.sh
		 * @return
		 */
		abstract boolean createEnv();
		/**
		 * create run.cmd||run.sh
		 * @return
		 */
		abstract boolean createRun();
		private void writeFile(String filename,String content) {
			FileUtils.writeStringToFile(pd+File.separatorChar+filename, content.toString());
		}
		private String getServerParam() {
			return isServerService?" -server ":"";
		}
		private String getJVMParam() {
			return !"".equals(param.trim())?" -D"+param:"";
		}
		private File[] getLibfiles() {
			return new File(lp).listFiles();
		}
		private String getLIB_PATH(){
			return system == LINUX?"$LIB_PATH":"%LIB_PATH%";
		}
		private String getLibpathStr() {
			return (lp.startsWith(pd)?"./"+lp.replaceAll(pd, ""):lp).replaceAll("//", "/");
		}
	}
	public class WindowsBatchCreator extends BatchCreator{

		String sep = ";";
		String wrap = String.valueOf((char)0x0D)+String.valueOf((char)0x0A); 
		public WindowsBatchCreator(int system, GenBat gb) {
			super(system, gb);
			if(ac !=null){
				ac = ac.replaceAll(":", sep);
			}
		}

		boolean createEnv() {
			File[] libfiles = super.getLibfiles();
			StringBuffer LIB_PATH = new StringBuffer();
			LIB_PATH.append(".").append(sep).append(super.getLIB_PATH()).append(sep);
			for(int i= 0;i<libfiles.length;i++){
				if(!libfiles[i].getName().endsWith(".jar"))
					continue;
				String name = super.getLIB_PATH()+File.separatorChar+libfiles[i].getName()+sep;
				LIB_PATH.append(name);
			}
			
			StringBuilder script = new StringBuilder();
			script.append("@rem *************************************************************************").append(wrap);
			script.append("@rem 在此设置JAVA_HOME,CLASS_PATH").append(wrap);
			script.append("@rem *************************************************************************").append(wrap);
			script.append("echo off").append(wrap);
			script.append("set JAVA_HOME=").append(jp).append(wrap);
			script.append("set LIB_PATH=").append(super.getLibpathStr()).append(wrap);
			script.append("set CLASS_PATH=").append(ac).append(";").append(LIB_PATH).append(wrap);
			super.writeFile("setEnv.cmd", script.toString());
			return true;
		}

		boolean createRun() {
			StringBuffer script = new StringBuffer();
			script.append("call setEnv.cmd").append(wrap);
			script.append("cmd.exe /k \"%JAVA_HOME%\\bin\\java\" ");
			script.append(super.getServerParam());
			script.append(super.getJVMParam());
			script.append(" -cp %CLASS_PATH% ").append(mc);
			super.writeFile(sfn+".cmd", script.toString());
			return true;
		}		
	}
	public class LinuxBatchCreator extends BatchCreator{

		String sep = ":";
		String wrap = String.valueOf((char)0x0A);
		public LinuxBatchCreator(int system, GenBat gb) {
			super(system, gb);
			if(ac !=null){
				ac = ac.replaceAll(";", sep);
			}
		}

		boolean createEnv() {
			File[] libfiles = super.getLibfiles();
			StringBuffer LIB_PATH = new StringBuffer();
			LIB_PATH.append(".").append(sep).append(super.getLIB_PATH()).append(sep);
			for(int i= 0;i<libfiles.length;i++){
				if(!libfiles[i].getName().endsWith(".jar"))
					continue;
				String name = super.getLIB_PATH()+"/"+libfiles[i].getName()+sep;
				LIB_PATH.append(name);
			}
			
			StringBuilder script = new StringBuilder();
			script.append("#!/bin/sh").append(wrap);
			script.append("#在此设置JAVA_HOME,CLASS_PATH").append(wrap);
			script.append("JAVA_HOME=").append(jp).append(wrap);
			script.append("LIB_PATH=").append(super.getLibpathStr()).append(wrap);
			script.append("CLASS_PATH=").append(ac).append(":").append(LIB_PATH).append(wrap);
			super.writeFile("setEnv.sh", script.toString());
			return true;
		}
		public boolean createRun() {
			StringBuffer script = new StringBuffer();
			script.append("#!/bin/sh").append(wrap);
			script.append(". \"setEnv.sh\"").append(wrap);
			script.append("$JAVA_HOME/bin/java");
			script.append(super.getServerParam());
			script.append(super.getJVMParam());
			script.append(" -classpath $CLASS_PATH ").append(mc);
			super.writeFile(sfn+".sh", script.toString());
			return true;
		}
	}
	

	public class LinuxWildBatchCreator extends BatchCreator{

		String sep = ":";
		String wrap = String.valueOf((char)0x0A);
		public LinuxWildBatchCreator(int system, GenBat gb) {
			super(system, gb);
			if(ac !=null){
				ac = ac.replaceAll(";", sep);
			}
		}

		boolean createEnv() {
			File[] libfiles = super.getLibfiles();
			StringBuffer LIB_PATH = new StringBuffer();
			LIB_PATH.append(".").append(sep).append(super.getLIB_PATH()).append(sep);
			for(int i= 0;i<libfiles.length;i++){
				if(!libfiles[i].getName().endsWith(".jar"))
					continue;
				String name = super.getLIB_PATH()+"/"+libfiles[i].getName()+sep;
				LIB_PATH.append(name);
			}
			
			StringBuilder script = new StringBuilder();
			script.append("#!/bin/sh").append(wrap);
			script.append("#在此设置JAVA_HOME,CLASS_PATH").append(wrap);;
			script.append("JAVA_HOME=").append(jp).append(wrap);
			script.append("LIB_PATH=").append(super.getLibpathStr()).append(wrap);
			script.append("for i in lib/*.jar SourceSys/*.jar").append(wrap);
			script.append("CLASS_PATH=").append(ac).append(":").append(LIB_PATH).append(wrap);
			super.writeFile("setEnv.sh", script.toString());
			return true;
		}
		public boolean createRun() {
			StringBuffer script = new StringBuffer();
			script.append("#!/bin/sh").append(wrap);
			script.append(". \"setEnv.sh\"").append(wrap);
			script.append("$JAVA_HOME/bin/java");
			script.append(super.getServerParam());
			script.append(super.getJVMParam());
			script.append(" -classpath $CLASS_PATH ").append(mc);
			super.writeFile(sfn+".sh", script.toString());
			return true;
		}
	}
}
