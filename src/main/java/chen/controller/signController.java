package chen.controller;

import chen.dome.domeSign;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/unidbg")
public class signController {

    @GetMapping("/sign/{data}")
    public String getSign(@PathVariable String data){
        domeSign domeSign = new domeSign();
        String s = domeSign.crack1(data);


        return s;
    }
}
