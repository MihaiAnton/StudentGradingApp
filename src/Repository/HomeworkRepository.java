package Repository;

import Domain.Homework;
import Validators.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class HomeworkRepository extends XMLRepository<Integer, Homework> {
    public HomeworkRepository(Validator validator, String fileName, String entity) {
        super(validator, fileName, entity);
    }

    @Override
    protected Homework getEntityFromElement(Element item, Document document) {
        int id = Integer.parseInt(item.getAttribute("id"));

        String description = item.getElementsByTagName("description").item(0).getChildNodes().item(0).getNodeValue();

        int targetWeek = Integer.parseInt(item.getElementsByTagName("targetWeek").item(0).getChildNodes().item(0).getNodeValue());

        int deadlineWeek = Integer.parseInt(item.getElementsByTagName("deadlineWeek").item(0).getChildNodes().item(0).getNodeValue());

        return new Homework(id,description,targetWeek,deadlineWeek,targetWeek);
    }

    @Override
    protected Node getEntityElement(Homework entity, Document document) {

        Element homeworkRoot = document.createElement("homework");

        homeworkRoot.setAttribute("id",""+entity.getId());
        homeworkRoot.appendChild(getLabelNode("description",entity.getDescription(),document));
        homeworkRoot.appendChild(getLabelNode("targetWeek",""+entity.getTargetWeek(),document));
        homeworkRoot.appendChild(getLabelNode("deadlineWeek",""+entity.getDeadlineWeek(),document));

        return homeworkRoot;
    }
}
