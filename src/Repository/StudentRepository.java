package Repository;

import Domain.Student;
import Validators.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StudentRepository extends XMLRepository<String, Student> {
    public StudentRepository(Validator validator, String fileName, String entity) {
        super(validator, fileName, entity);
    }

    @Override
    protected Student getEntityFromElement(Element item, Document document) {
        String id = item.getAttribute("id");
        NodeList nodeList;

        nodeList = item.getElementsByTagName("name");
        String name = nodeList.item(0).getChildNodes().item(0).getNodeValue();

        nodeList = item.getElementsByTagName("group");
        int group = Integer.parseInt(nodeList.item(0).getChildNodes().item(0).getNodeValue());

        nodeList = item.getElementsByTagName("email");
        String email = nodeList.item(0).getChildNodes().item(0).getNodeValue();

        nodeList = item.getElementsByTagName("teacher");
        String teacher = nodeList.item(0).getChildNodes().item(0).getNodeValue();

        return new Student(id,name,group,email,teacher);
    }

    @Override
    protected Node getEntityElement(Student entity, Document document) {
        //create the root
        Element studentRoot = document.createElement("student");

        //set id attribute
        studentRoot.setAttribute("id",entity.getId());

        //add name
        studentRoot.appendChild(getLabelNode("name",entity.getName(),document));

        //add group
        studentRoot.appendChild(getLabelNode("group","" + entity.getGroup(),document));

        //add email
        studentRoot.appendChild(getLabelNode("email",entity.getEmail(),document));

        //add teacher
        studentRoot.appendChild(getLabelNode("teacher",entity.getTeacher(),document));

        return studentRoot;
    }
    
    
}
