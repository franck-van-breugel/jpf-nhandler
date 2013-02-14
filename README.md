JPF-NHANDLER
============

jpf-nhandler is an extension of Java PathFinder (JPF). It automatically 
delegates the execution of SUT methods from JPF to the host JVM. Execution 
of a call o.m(a) delegated by jpf-nhandler follows three main steps:

  1. It transforms the JPF representation of o and a to the host JVM 
     level.

  2. It delegates the execution to the original (non-native or native) 
     method m by invoking it on the host JVM.

  3. Finally, it transforms the result of the method call back to its 
     JPF representation.

The implementation of jpf-nhandler mostly relies on MJI. jpf-nhandler 
creates bytecode for native peers on-the-fly (they are OTF peer from 
now on) using the BCEL library. To delegate the execution of a method to 
the host JVM, jpf-nhandler adds a method in the corresponding OTF native 
peer which implements the three steps described above.

The main applications of jpf-nhandler:

  1. The key application of jpf-nhandler is to automatically intercept
     and handle native calls within JPF. This extends the JPF functionality 
     considerably, since it allows JPF to verify numerous SUTs on which 
     JPF otherwise would crash.

     is executed outside of JPF, in its normal environment. Hence, this 
     tool can be used to reduce the state space and improve the scalability 
     of JPF.

  3. JPF creates execution traces as it runs the SUT. Long traces can 
     cause JPF to run out of memory. In such cases, jpf-nhandler can be 
     used to delegate methods with long traces, and execute them on the 
     host JVM.

  4. Delegating a method may also speed up JPF.

jpf-nhandler can be configured in variety of ways. Here are some examples:

  - It can be used to skip calls instead of delegating them. In this case 
    methods are executed as if they are empty and they just return some 
    dummy value.

  - It also provides a way to specify which methods are delegated or skipped.
    To force JPF to delegate the constructor of the class a.b.C, use

        nhandler.spec.delegate = a.b.C.<init>

    To force JPF to delegate all method in the String class, use

        nhandler.spec.delegate = java.lang.String.*

    To force JPF to skip java.io.FileDescriptor.write(), use

        nhandler.spec.skip = java.io.FileDescriptor.write

  - jpf-nhandler can also be configured to only delegate native calls which 
    are not handled in JPF

  - jpf-nhandler can be also configured to generate source code for OTF 
    peers on-the-fly, which allows the user to subsequently refine its 
    implementation.

  - Since on-the-fly bytecode generation is expensive, one can also configure 
    jpf-nhandler to retain and reuse OTF peers for future runs, i.e. their 
    body may be extended as jpf-nhandler delegates more calls in the future.

Limitations of jpf-nhandler
---------------------------

  1. The implementation of some classes is platform-specific, for instance 
     java.lang.System. jpf-nhandler cannot be used for such classes due to 
     inconsistencies between JPF and the host JVM.

  2. Since jpf-nhandler relies on transforming objects and classes between 
     JPF and the host JVM, the state of a class or object should consist 
     of the same fields and superclasses in both the host JVM and JPF. This 
     limits the application of jpf-nhandler for types with JPF model classes 
     that are inconsistent with the actual class in the Java library.

  3. The side effects of the delegated method should be only observable through 
     the return value, the arguments of the method, and the object or class
     invoking the method, e.g. the lock() method in the class 
     java.util.concurrent.locks.ReentrantLock cannot be handled.

  4. jpf-nhandler cannot handle certain objects of which part of their state 
     is kept natively, e.g. java.awt.Window


Licensing of jpf-nhandler
-------------------------

This extension is free software: you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by the Free 
Software Foundation, either version 3 of the License, or (at your option) 
any later version.

This extension is distributed in the hope that it will be useful, but WITHOUT 
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You can find a copy of the GNU General Public License at
http://www.gnu.org/licenses


Installing of jpf-nhandler
--------------------------

To install jpf-nhandler, follow the steps below.

1. Install JPF.
   See http://babelfish.arc.nasa.gov/trac/jpf/wiki/install/start

2. Install jpf-nhandler.
   The source for jpf-nhandler: https://bitbucket.org/nastaran/jpf-nhandler
   See http://babelfish.arc.nasa.gov/trac/jpf/wiki/install/repositories

3. Build jpf-nhandler using ant.
   See http://babelfish.arc.nasa.gov/trac/jpf/wiki/install/build

4. Add jpf-nhandler to the file site.properties.
   See http://babelfish.arc.nasa.gov/trac/jpf/wiki/install/site-properties


Running JPF with jpf-nhandler
-----------------------------

To run JPF on the class Example the *.jpf file includes

@using = jpf-nhandler
target = Example
nhandler.delegateUnhandledNative = true
classpath = path-to-application-classes
native_classpath = path-to-application-classes

Note that to use jpf-nhandler, the classes used in the system under test 
should be specified both in classpath and native_classpath. Because The 
execution goes back and forth between JPF and the underlying host JVM, 
therefore both JPF and the host JVM should be able to access these classes.


Questions/comments/suggestions
------------------------------

Please email them to nastaran.shafiei@gmail.com


Thanks
------

to Peter Mehlitz for his help with the development of jpf-nhandler