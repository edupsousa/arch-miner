/**
 * Scaffolding file used to store all the setups needed to run 
 * tests automatically generated by EvoSuite
 * Thu Nov 29 17:21:01 GMT 2018
 */

package persistence;

import org.evosuite.runtime.annotation.EvoSuiteClassExclude;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.After;
import org.junit.AfterClass;
import org.evosuite.runtime.sandbox.Sandbox;
import org.evosuite.runtime.sandbox.Sandbox.SandboxMode;

@EvoSuiteClassExclude
public class SQLFile_ESTest_scaffolding {

  @org.junit.Rule 
  public org.evosuite.runtime.vnet.NonFunctionalRequirementRule nfr = new org.evosuite.runtime.vnet.NonFunctionalRequirementRule();

  private static final java.util.Properties defaultProperties = (java.util.Properties) java.lang.System.getProperties().clone(); 

  private org.evosuite.runtime.thread.ThreadStopper threadStopper =  new org.evosuite.runtime.thread.ThreadStopper (org.evosuite.runtime.thread.KillSwitchHandler.getInstance(), 3000);


  @BeforeClass 
  public static void initEvoSuiteFramework() { 
    org.evosuite.runtime.RuntimeSettings.className = "persistence.SQLFile"; 
    org.evosuite.runtime.GuiSupport.initialize(); 
    org.evosuite.runtime.RuntimeSettings.maxNumberOfThreads = 100; 
    org.evosuite.runtime.RuntimeSettings.maxNumberOfIterationsPerLoop = 10000; 
    org.evosuite.runtime.RuntimeSettings.mockSystemIn = true; 
    org.evosuite.runtime.RuntimeSettings.sandboxMode = org.evosuite.runtime.sandbox.Sandbox.SandboxMode.RECOMMENDED; 
    org.evosuite.runtime.sandbox.Sandbox.initializeSecurityManagerForSUT(); 
    org.evosuite.runtime.classhandling.JDKClassResetter.init();
    setSystemProperties();
    initializeClasses();
    org.evosuite.runtime.Runtime.getInstance().resetRuntime(); 
  } 

  @AfterClass 
  public static void clearEvoSuiteFramework(){ 
    Sandbox.resetDefaultSecurityManager(); 
    java.lang.System.setProperties((java.util.Properties) defaultProperties.clone()); 
  } 

  @Before 
  public void initTestCase(){ 
    threadStopper.storeCurrentThreads();
    threadStopper.startRecordingTime();
    org.evosuite.runtime.jvm.ShutdownHookHandler.getInstance().initHandler(); 
    org.evosuite.runtime.sandbox.Sandbox.goingToExecuteSUTCode(); 
    setSystemProperties(); 
    org.evosuite.runtime.GuiSupport.setHeadless(); 
    org.evosuite.runtime.Runtime.getInstance().resetRuntime(); 
    org.evosuite.runtime.agent.InstrumentingAgent.activate(); 
  } 

  @After 
  public void doneWithTestCase(){ 
    threadStopper.killAndJoinClientThreads();
    org.evosuite.runtime.jvm.ShutdownHookHandler.getInstance().safeExecuteAddedHooks(); 
    org.evosuite.runtime.classhandling.JDKClassResetter.reset(); 
    resetClasses(); 
    org.evosuite.runtime.sandbox.Sandbox.doneWithExecutingSUTCode(); 
    org.evosuite.runtime.agent.InstrumentingAgent.deactivate(); 
    org.evosuite.runtime.GuiSupport.restoreHeadlessMode(); 
  } 

  public static void setSystemProperties() {
 
    java.lang.System.setProperties((java.util.Properties) defaultProperties.clone()); 
    java.lang.System.setProperty("file.encoding", "UTF-8"); 
    java.lang.System.setProperty("java.awt.headless", "true"); 
    java.lang.System.setProperty("java.io.tmpdir", "/tmp"); 
    java.lang.System.setProperty("user.country", "US"); 
    java.lang.System.setProperty("user.dir", "/home/edupsousa/Desktop/EvoSuite/arch-miner"); 
    java.lang.System.setProperty("user.home", "/home/edupsousa"); 
    java.lang.System.setProperty("user.language", "en"); 
    java.lang.System.setProperty("user.name", "edupsousa"); 
    java.lang.System.setProperty("user.timezone", "America/Sao_Paulo"); 
  }

  private static void initializeClasses() {
    org.evosuite.runtime.classhandling.ClassStateSupport.initializeClasses(SQLFile_ESTest_scaffolding.class.getClassLoader() ,
      "org.apache.commons.text.translate.AggregateTranslator",
      "org.apache.commons.lang3.Range$ComparableComparator",
      "org.apache.commons.text.translate.EntityArrays",
      "org.apache.commons.text.translate.OctalUnescaper",
      "org.apache.commons.text.translate.CharSequenceTranslator",
      "org.apache.commons.text.translate.CsvTranslators$CsvUnescaper",
      "persistence.SQLFile",
      "org.apache.commons.text.translate.CsvTranslators",
      "org.apache.commons.text.translate.UnicodeUnescaper",
      "org.repodriller.RepoDrillerException",
      "org.apache.commons.text.translate.NumericEntityEscaper",
      "org.apache.commons.lang3.Range",
      "org.apache.commons.text.translate.UnicodeUnpairedSurrogateRemover",
      "org.apache.commons.text.translate.SinglePassTranslator",
      "org.apache.commons.text.translate.JavaUnicodeEscaper",
      "org.apache.commons.text.StringEscapeUtils",
      "org.apache.commons.text.translate.CodePointTranslator",
      "org.apache.commons.text.translate.NumericEntityUnescaper",
      "org.apache.commons.text.StringEscapeUtils$XsiUnescaper",
      "org.apache.commons.text.StringEscapeUtils$Builder",
      "org.apache.commons.lang3.Validate",
      "org.apache.commons.text.translate.NumericEntityUnescaper$OPTION",
      "org.repodriller.util.PathUtils",
      "org.apache.commons.text.translate.CsvTranslators$CsvEscaper",
      "org.repodriller.persistence.PersistenceMechanism",
      "org.apache.commons.text.translate.LookupTranslator",
      "org.apache.commons.text.translate.UnicodeEscaper"
    );
  } 

  private static void resetClasses() {
    org.evosuite.runtime.classhandling.ClassResetter.getInstance().setClassLoader(SQLFile_ESTest_scaffolding.class.getClassLoader()); 

    org.evosuite.runtime.classhandling.ClassStateSupport.resetClasses(
      "persistence.SQLFile",
      "org.apache.commons.text.translate.EntityArrays",
      "org.apache.commons.text.translate.CharSequenceTranslator",
      "org.apache.commons.text.translate.AggregateTranslator",
      "org.apache.commons.text.translate.LookupTranslator",
      "org.apache.commons.text.translate.CodePointTranslator",
      "org.apache.commons.text.translate.UnicodeEscaper",
      "org.apache.commons.text.translate.JavaUnicodeEscaper",
      "org.apache.commons.text.translate.NumericEntityEscaper",
      "org.apache.commons.lang3.Range",
      "org.apache.commons.lang3.Range$ComparableComparator",
      "org.apache.commons.text.translate.UnicodeUnpairedSurrogateRemover",
      "org.apache.commons.text.translate.SinglePassTranslator",
      "org.apache.commons.text.translate.CsvTranslators$CsvEscaper",
      "org.apache.commons.text.translate.OctalUnescaper",
      "org.apache.commons.text.translate.UnicodeUnescaper",
      "org.apache.commons.text.translate.NumericEntityUnescaper",
      "org.apache.commons.text.translate.NumericEntityUnescaper$OPTION",
      "org.apache.commons.text.translate.CsvTranslators$CsvUnescaper",
      "org.apache.commons.text.StringEscapeUtils$XsiUnescaper",
      "org.apache.commons.text.StringEscapeUtils",
      "org.repodriller.util.PathUtils",
      "org.repodriller.RepoDrillerException",
      "org.apache.commons.lang3.Validate"
    );
  }
}
