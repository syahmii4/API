package org.finalecorp.scorelabs;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {
    @RequestMapping("/api")
    public String hemlo(){
        return "hemlo ammar";
    }
}
