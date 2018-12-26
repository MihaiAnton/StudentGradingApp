package Repository;

import Domain.Grade;
import Validators.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class GradeRepository extends XMLRepository<String, Grade> {
    public GradeRepository(Validator validator, String fileName, String entity) {
        super(validator, fileName, entity);
    }

    @Override
    protected  Grade getEntityFromElement(Element item, Document document) {

        String sid = item.getElementsByTagName("studentId").item(0).getChildNodes().item(0).getNodeValue();

        String hid = item.getElementsByTagName("homeworkId").item(0).getChildNodes().item(0).getNodeValue();

        double grade = Double.parseDouble(item.getElementsByTagName("gr").item(0).getChildNodes().item(0).getNodeValue());

        String feedback = item.getElementsByTagName("feedback").item(0).getChildNodes().item(0).getNodeValue();

        int week = Integer.parseInt(item.getElementsByTagName("week").item(0).getChildNodes().item(0).getNodeValue());

        Grade grade1 = new  Grade(sid,Integer.parseInt(hid), grade, feedback);
        grade1.setWeek(week);

        return grade1;
    }

    @Override
    protected Node getEntityElement(Grade entity, Document document) {
        Element homeworkRoot = document.createElement("grade");

        homeworkRoot.setAttribute("id",""+entity.getId());
        homeworkRoot.appendChild(getLabelNode("studentId",entity.getStudId(),document));
        homeworkRoot.appendChild(getLabelNode("homeworkId",""+entity.getHomeworkId(),document));
        homeworkRoot.appendChild(getLabelNode("gr",String.valueOf(entity.getGrade()),document));
        homeworkRoot.appendChild(getLabelNode("feedback", entity.getFeedback(), document));
        homeworkRoot.appendChild(getLabelNode("week", ""+entity.getWeek(), document));

        return homeworkRoot;
    }
}
