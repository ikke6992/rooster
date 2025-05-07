package nl.itvitae.rooster;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectController {

  @RequestMapping("/rooster/**")
  public String forward() {
    return "forward:/index.html";
  }
}