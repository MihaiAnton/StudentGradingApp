package Repository;

import Domain.Password;
import Validators.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LoginInfo extends XMLRepository<String, Password> {


    public LoginInfo(Validator validator, String fileName, String entity) {
        super(validator, fileName, entity);
    }

    @Override
    protected Password getEntityFromElement(Element item, Document document) {
        Password password = new Password();

        String id = item.getAttribute("id");
        String pass = item.getElementsByTagName("cryptedPass").item(0).getChildNodes().item(0).getNodeValue();

        password.setId(id);
        password.setPassword(pass);

        return password;
    }

    @Override
    protected Node getEntityElement(Password entity, Document document) {

        Element root = document.createElement("password");
        root.setAttribute("id", entity.getId());
        root.appendChild(getLabelNode("cryptedPass",entity.getPassword(),document));

        return root;
    }
}
