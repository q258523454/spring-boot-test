package com.circle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.circle",
        })
public class CircleApplication {

    /**
     * 解决循环依赖:
     *  1.@Autowired
     *  2.setter方法
     * 报错:
     *  构造方法(单例模式报错)
     *  问: Spring循环依赖有哪些解决方式?
     * 答:
     *    依赖情况      依赖注入方式                循环依赖是否被解决
     *    AB相互依赖 	均采用Autowired                	是
     *    AB相互依赖 	均采用setter方法注入           	是
     *    AB相互依赖 	A中Setter(B),B中Construct(A)   	是,注意A会先实例化.
     *    AB相互依赖 	均采用构造器注入              	否,启动报错,原因:构造方法在实例化阶段.此时, 都没有对象存在.
     *    AB相互依赖 	B中Setter(A),A中Construct(B)   	否,启动报错,原因:1.按字母顺序实例化;2.实例化在前,setter注入在后
     *
     * 问: Spring循环依赖解决原理?
     * 答: 本质是: 对象实例化之后,会在三级缓存里加入一个工厂,提前对外暴露还未完整的 Bean ,破坏循环。
     *       三级缓存的是bean的代理对象，通过代理来跳过循环依赖中某个实例的实例化阶段。
     *       以A注入B, B注入A为例. A完成实例化后,立马setter注入B。 此时B又循环依赖与A。
     *       由于可以拿到A实例化后的代理对象，因此不需要完成A的初始化。直接走完B的流程。
     *       最后再继续完成A的流程。
     *       Spring通过三级缓存解决了循环依赖，其中一级缓存为单例池(singletonObjects),
     *       二级缓存为早期曝光对象earlySingletonObjects，三级缓存为早期曝光对象工厂(singletonFactories)。
     *       当A、B两个类发生循环引用时，在A完成实例化后，就使用实例化后的对象去创建一个对象工厂，并添加到三级缓存中，
     *       如果A被AOP代理，那么通过这个工厂获取到的就是A代理后的对象，如果A没有被AOP代理，那么这个工厂获取到的就是
     *       A实例化的对象。当A进行属性注入时，会去创建B，同时B又依赖了A，所以创建B的同时又会去调用getBean(a)来获取
     *       需要的依赖，此时的getBean(a)会从缓存中获取，
     *       第一步，先获取到三级缓存中的工厂；
     *       第二步，调用对象工工厂的getObject方法来获取到对应的对象，得到这个对象后将其注入到B中。
     *       紧接着B会走完它的生命周期流程，包括初始化、后置处理器等。当B创建完后，会将B再注入到A中，
     *       此时A再完成它的整个生命周期。至此，循环依赖结束!
     *
     * 问: 为什么要使用三级缓存呢？二级缓存能解决循环依赖吗？
     * 答: 如果仅仅只是为了解决循环依赖, 通过上面所述, 用两个缓存就够了.
     *     但是要使用二级缓存解决循环依赖，意味着所有Bean在实例化后就要完成AOP代理，
     *     这样违背了Spring设计的原则，Spring在设计之初就是通过后置处理器来在Bean生命周期的最后一步来完成AOP代理，
     *     而不是在实例化后就立马进行AOP代理。用三级缓存就是为了尽量不打破bean的生命周期,仅此而已.
     *
     */
    public static void main(String[] args) {
        SpringApplication.run(CircleApplication.class, args);
    }
}