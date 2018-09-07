# avian_null_dynamicTable

A MCV example Java app that crashes Avian JVM.

Avian is built with:

```bash
    make --trace platform=linux process=compile mode=fast openjdk=$JDK_INSTALL openjdk-src=$JDK_SOURCE
```

with the JDK being '8 u171 b11'

Build with:

```bash
    ./gradlew build
```

which will result in:


```bash
    AvianCrash/build/distributions/AvianCrash-1.0.tar and .zip
```

Run either of those apps on Avian:

```bash
    ${AVIAN_BUILD_DIR}/avian-dynamic \
	-Djava.library.path=${AVIAN_BUILD_DIR}/libjvm.so \
	-Djava.security.debug="jca,provider" \
	-classpath ${CLASSPATH} \
	com.example.Main
```
where $CLASSPATH points to the contents of the 'lib' dir inside the built archives.

You should see a crash:

```text
DelayedEvent thread started
register
waiting for crash...
notifyListener
eventHandler called
./run.sh: line 46: 14325 Aborted                 (core dumped) ${AVIAN_BUILD_DIR}/avian-dynamic -Djava.library.path=${AVIAN_BUILD_DIR}/libjvm.so -Djava.security.debug="jca,provider" -classpath ${CLASSPATH} com.example.Main
```
with the following details from lldb:

```text
Process 14395 stopped
* thread #3: tid = 14399, 0x00007ffff7af5564 libjvm.so`(anonymous namespace)::local::addDynamic(t=0x0000555555971c58, invocation=0x00007fffe401e758)::local::MyThread *, vm::GcInvocation *) + 1482 at compile.cpp:1373, name = 'avian-dynamic', stop reason = signal SIGSEGV: invalid address (fault address: 0x8)
    frame #0: 0x00007ffff7af5564 libjvm.so`(anonymous namespace)::local::addDynamic(t=0x0000555555971c58, invocation=0x00007fffe401e758)::local::MyThread *, vm::GcInvocation *) + 1482 at compile.cpp:1373
   1370	    compileRoots(t)->dynamicThunks()->body()[index * 2] = thunk;
   1371	    compileRoots(t)->dynamicThunks()->body()[(index * 2) + 1] = size;
   1372	
-> 1373	    t->dynamicTable[index] = reinterpret_cast<void*>(thunk);
   1374	
   1375	    roots(t)->invocations()->setBodyElement(t, index, invocation);
   1376	  }
(lldb) thread backtrace
* thread #3: tid = 14399, 0x00007ffff7af5564 libjvm.so`(anonymous namespace)::local::addDynamic(t=0x0000555555971c58, invocation=0x00007fffe401e758)::local::MyThread *, vm::GcInvocation *) + 1482 at compile.cpp:1373, name = 'avian-dynamic', stop reason = signal SIGSEGV: invalid address (fault address: 0x8)
  * frame #0: 0x00007ffff7af5564 libjvm.so`(anonymous namespace)::local::addDynamic(t=0x0000555555971c58, invocation=0x00007fffe401e758)::local::MyThread *, vm::GcInvocation *) + 1482 at compile.cpp:1373
    frame #1: 0x00007ffff7b0638f libjvm.so`(anonymous namespace)::local::compile(t=0x0000555555971c58, initialFrame=0x00007ffff0de8ed0, initialIp=0, exceptionHandlerStart=-1)::local::MyThread *, (anonymous namespace)::local::Frame *, unsigned int, int) + 21271 at compile.cpp:5289
    frame #2: 0x00007ffff7b0e96e libjvm.so`(anonymous namespace)::local::compile(t=0x0000555555971c58, context=0x00007ffff0de9040)::local::MyThread *, (anonymous namespace)::local::Context *) + 1242 at compile.cpp:7378
    frame #3: 0x00007ffff7b2375e libjvm.so`(anonymous namespace)::local::compile(t=0x0000555555971c58, allocator=0x000055555575d290, bootContext=0x0000000000000000, method=0x00007fffe401f5a0)::local::MyThread *, avian::util::FixedAllocator *, (anonymous namespace)::local::BootContext *, vm::GcMethod *) + 488 at compile.cpp:10664
    frame #4: 0x00007ffff7b15bd5 libjvm.so`(anonymous namespace)::local::MyProcessor::invokeList(this=0x000055555575d1e8, t=0x0000555555971c58, method=0x00007fffe401f5a0, this_=0x0000000000000000, indirectObjects=false, arguments=0x00007ffff0de9320) const + 757 at compile.cpp:9187
    frame #5: 0x00007ffff7ad110b libjvm.so`vm::Processor::invoke(this=0x000055555575d1e8, t=0x0000555555971c58, method=0x00007fffe401f5a0, this_=0x0000000000000000) + 215 at processor.h:207
    frame #6: 0x00007ffff7ab54f8 libjvm.so`vm::initClass(t=0x0000555555971c58, c=0x00007fffe401f6a0) + 312 at machine.cpp:5267
    frame #7: 0x00007ffff7b2362b libjvm.so`(anonymous namespace)::local::compile(t=0x0000555555971c58, allocator=0x000055555575d290, bootContext=0x0000000000000000, method=0x00007fffe401f458)::local::MyThread *, avian::util::FixedAllocator *, (anonymous namespace)::local::BootContext *, vm::GcMethod *) + 181 at compile.cpp:10633
    frame #8: 0x00007ffff7b1780c libjvm.so`(anonymous namespace)::local::compileMethod2(t=0x0000555555971c58, ip=0x000000004005b08c)::local::MyThread *, void *) + 248 at compile.cpp:9645
    frame #9: 0x00007ffff7b0f02d libjvm.so`(anonymous namespace)::local::compileMethod(t=0x0000555555971c58)::local::MyThread *) + 110 at compile.cpp:7472
    frame #10: 0x000000004000001b
    frame #11: 0x00007ffff7b13337 libjvm.so`(anonymous namespace)::local::invoke(thread=0x0000555555971c58, method=0x00007ffff10def78, arguments=0x00007ffff0de9b70)::local::ArgumentList *) + 597 at compile.cpp:8555
    frame #12: 0x00007ffff7b15bf2 libjvm.so`(anonymous namespace)::local::MyProcessor::invokeList(this=0x000055555575d1e8, t=0x0000555555971c58, method=0x00007ffff10def78, this_=0x0000555555897460, indirectObjects=false, arguments=0x00007ffff0de9c20) const + 786 at compile.cpp:9189
    frame #13: 0x00007ffff7ad110b libjvm.so`vm::Processor::invoke(this=0x000055555575d1e8, t=0x0000555555971c58, method=0x00007ffff10def78, this_=0x0000555555897460) + 215 at processor.h:207
    frame #14: 0x00007ffff7b2fda7 libjvm.so`(anonymous namespace)::local::MyClasspath::runThread(this=0x0000555555758538, t=0x0000555555971c58) const + 179 at classpath-openjdk.cpp:622
    frame #15: 0x00007ffff7ae0c50 libjvm.so`vm::runJavaThread(t=0x0000555555971c58) + 73 at machine.h:1806
    frame #16: 0x00007ffff7ae0cff libjvm.so`vm::runThread(t=0x0000555555971c58, (null)=0x0000000000000000) + 152 at machine.h:1820
    frame #17: 0x00007ffff7b7efe6 libjvm.so`vmRun + 47
    frame #18: 0x00007ffff7ae0b5a libjvm.so`vm::runRaw(t=0x0000555555971c58, function=(libjvm.so`vm::runThread(vm::Thread*, unsigned long*) at machine.h:1812), arguments=0x0000000000000000)(vm::Thread*, unsigned long*), unsigned long*) + 78 at machine.h:1793
    frame #19: 0x00007ffff7ae0bda libjvm.so`vm::run(t=0x0000555555971c58, function=(libjvm.so`vm::runThread(vm::Thread*, unsigned long*) at machine.h:1812), arguments=0x0000000000000000)(vm::Thread*, unsigned long*), unsigned long*) + 83 at machine.h:1801
    frame #20: 0x00007ffff7ae096c libjvm.so`vm::Thread::Runnable::run(this=0x0000555555971ce0) + 72 at machine.h:1769
    frame #21: 0x00007ffff7a94c01 libjvm.so`(anonymous namespace)::run(r=0x0000555555971ce0) + 50 at posix.cpp:101
    frame #22: 0x00007ffff6f846ba libpthread.so.0`start_thread + 202
    frame #23: 0x00007ffff77c441d libc.so.6`__clone + 109 at clone.S:109
```




