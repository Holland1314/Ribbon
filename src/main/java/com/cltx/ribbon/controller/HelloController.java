package com.cltx.ribbon.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by shileichao on 2018/5/30.
 */
@RestController
public class HelloController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @RequestMapping(value = "/hi")
    @HystrixCommand(fallbackMethod="hiFallback")
    public String hi(@RequestParam("id") String id) {

        // ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://SPRINGBOOT-CLIENT/user", String.class);
        //return  restTemplate.getForObject("http://127.0.0.1:9091/user/findById?id="+id,String.class);
        return restTemplate.getForObject("http://SPRINGBOOT-CLIENT/user/findById?id=" + id, String.class);

    }

    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public String getUser(@RequestParam("id") String id) {
        this.loadBalancerClient.choose("SPRINGBOOT-CLIENT");
        return restTemplate.getForEntity("http://SPRINGBOOT-CLIENT/user/findById?id=" + id, String.class).getBody();
    }
    public String hiFallback(String id){
        return "hi方法远程调用超市" + id;
    }

}
