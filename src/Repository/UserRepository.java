package Repository;

import Domain.AccesRight;
import Domain.User;
import Validators.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UserRepository extends XMLRepository<String, User> {

    private int nextId;
    public UserRepository(Validator validator, String fileName, String entity) {
        super(validator, fileName, entity);
        nextId = 1;
    }

    @Override
    protected User getEntityFromElement(Element item, Document document) {


        String userName = item.getAttribute("id");
        NodeList nodeList;
        nodeList = item.getElementsByTagName("accesRight");
        AccesRight right = AccesRight.valueOf(nodeList.item(0).getChildNodes().item(0).getNodeValue());

        return new User(userName, right);

    }

    @Override
    protected Node getEntityElement(User entity, Document document) {

        Element root = document.createElement("user");

        root.setAttribute("id",entity.getId());

        root.appendChild(getLabelNode("accesRight",""+entity.getRight(),document));

        return root;
    }


    public int getNextId(){
        this.nextId ++;
        return this.nextId-1;
    }
}





















