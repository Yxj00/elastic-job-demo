package com.csi;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

public class JobDemo {
    public static void main(String[] args) {
//        JobScheduler(注册中心对象，任务配置对象)
        new JobScheduler(createRegistryCenter(),
                createJobConfiguration()).init();
    }
//定时任务类配置
    private static LiteJobConfiguration createJobConfiguration() {
        // 定义作业核⼼配置newBuilder("任务名称","cron表达式","分片数量")
        JobCoreConfiguration simpleCoreConfig =
                JobCoreConfiguration.newBuilder("myElasticJob","0/3 * * * * ?",1).build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new
                SimpleJobConfiguration(simpleCoreConfig,
                MyElasticJob.class.getCanonicalName());
        // 定义Lite作业根配置  overwrite可覆盖旧属性
        LiteJobConfiguration simpleJobRootConfig =
                LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(true).build();
        return simpleJobRootConfig;

    }
//注册中心配置
    private static CoordinatorRegistryCenter createRegistryCenter() {

        ZookeeperConfiguration zookeeperConfiguration = new
                ZookeeperConfiguration("localhost:2181", "elastic-job-demo");
        //设置节点超时时间
        zookeeperConfiguration.setSessionTimeoutMilliseconds(100);
        //        ZookeeperRegistryCenter("zookeeper地址","项目名")
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(zookeeperConfiguration);
        regCenter.init();
        return regCenter;
    }
}
