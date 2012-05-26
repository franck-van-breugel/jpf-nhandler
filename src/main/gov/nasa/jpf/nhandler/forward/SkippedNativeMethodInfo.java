package gov.nasa.jpf.nhandler.forward;

import gov.nasa.jpf.jvm.MJIEnv;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.NativeMethodInfo;
import gov.nasa.jpf.jvm.NativePeer;
import gov.nasa.jpf.nhandler.peerGen.PeerClassGen;

/**
 * This is a subclass of NativeMethodInfo which makes executeNative() skip the
 * execution of the unhandled native method by executing the corresponding empty 
 * method at the JVM level which returns a dummy value.
 * 
 * @author Nastaran Shafiei
 * @author Franck van Breugel
 */
public class SkippedNativeMethodInfo extends NativeMethodInfo {

  public SkippedNativeMethodInfo (MethodInfo mi) {
    super(mi, null, null);
  }

  protected boolean isUnsatisfiedLinkError (MJIEnv env){
    if (mth == null){
      System.out.println("* SKIPPING Unhandled Native -> " + this.ci.getName() + "." + this.name);
      PeerClassGen peerCreator = PeerClassGen.getPeerCreator(this.getClassInfo(), env);
      mth = peerCreator.createEmptyMethod(this);
      this.peer = new NativePeer(peerCreator.getPeer(), this.ci);
      assert (this.peer != null && mth != null);
    }

    return false;
  }
}
