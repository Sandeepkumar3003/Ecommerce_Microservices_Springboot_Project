package com.info.configdemo;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BuildInfoController {
//    @Value("${build.id}")
//    private String buildId;
//
//    @Value("${build.version}")
//    private String buildVersion;
//
//    @Value("${build.name}")
//    private String buildName;

    private BuildInfo buildInfo;

    @GetMapping("/build-info")
    public String getBuildInfo() {
        return "Build ID: " + buildInfo.getId() + ", Version " + buildInfo.getVersion() + ", Name: " + buildInfo.getName();
    }
}
