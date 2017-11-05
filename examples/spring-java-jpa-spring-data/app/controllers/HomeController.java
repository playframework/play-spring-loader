package controllers;

import play.mvc.*;
import java.util.Random;
import javax.inject.Inject;
import model.MyEntity;
import repositories.MyEntityRepository;
import org.springframework.stereotype.Component;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Component
public class HomeController extends Controller {

    @Inject
    private MyEntityRepository repository;

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
		MyEntity entity = new MyEntity();
		entity.setEmail("test"+new Random().nextInt()+"@test.com");
		repository.save(entity);
        return ok(views.html.index.render(repository.findAll()));
    }
}