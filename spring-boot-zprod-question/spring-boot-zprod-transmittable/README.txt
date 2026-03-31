
TransmittableThreadLocal 问题

一、前言：
    解决父子线程间值传递的方法：
    ① InheritableThreadLocal
    局现：
    - 只支持创建新线程时的值传递
    - 线程池场景下对某个线程只能传递一次
      即：父线程中共享变量的值改变，已赋值的线程中的共享变量值不会改变

    ② TransmittableThreadLocal
    - 支持线程池场景下的值传递
    - 兼容InheritableThreadLocal
    - 必须用到TTL包装类(TtlExecutors、TtlRunnable、TtlCallable)
    注:如果不使用TTL,则会退化成 InheritableThreadLocal

二、问题
    描述：
    TransmittableThreadLocal 如果使用默认的线程池 会退化成 InheritableThreadLocal, 存在数据污染。
    一旦子线程被传递赋值，下次使用该线程的时候 TransmittableThreadLocal 不会因为主线程的修改而改变.

    假设有3个子线程
    请求(出现故障)：
    -----------------------------------------------------------
     请求                 主线程                子线程
    traceId=1    mainTread: traceId=1      subTread-1: traceId=1
    traceId=2    mainTread: traceId=2      subTread-2: traceId=1
    traceId=3    mainTread: traceId=3      subTread-3: traceId=1
    -----------------------------------------------------------
    traceId=4    mainTread: traceId=4      subTread-1: traceId=1 (注意 traceId 还是上次复制的值)
    traceId=5    mainTread: traceId=5      subTread-2: traceId=1 (注意 traceId 还是上次复制的值)
    traceId=6    mainTread: traceId=6      subTread-3: traceId=1 (注意 traceId 还是上次复制的值)
    ....
    -----------------------------------------------------------

三、解决:
    方案1：使用增强版TTL Executors:  TtlExecutors
    ExecutorService executor= TtlExecutors.getTtlExecutorService(threadPoolExecutor);

    方案2: 使用增强版TTL Callable: TtlCallable
    XxxThreadPool.getExecutor().submit(TtlCallable.get()...)













