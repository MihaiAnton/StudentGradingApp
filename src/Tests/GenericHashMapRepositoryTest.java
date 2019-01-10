package Tests;

import Domain.IdEntity;
import Repository.GenericHashMapRepository;
import Validators.GenericValidator;
import Validators.Validator;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenericHashMapRepositoryTest {

    class testItem implements IdEntity<String>{

        private String id;

        public testItem(String id){
            this.id = id;
        }

        @Override
        public void setId(String s) {
            this.id = s;
        }

        @Override
        public String getId() {
            return id;
        }
    }

    class TestRepo extends GenericHashMapRepository<String,testItem>{

        public TestRepo(Validator<testItem> valid) {
            super(valid);
        }
    };

    private TestRepo repo;

    @BeforeEach
    void setUp() {
        this.repo = new TestRepo(new GenericValidator<testItem>());
        this.repo.save(new testItem("a"));
        this.repo.save(new testItem("b"));
        this.repo.save(new testItem("c"));
    }

    @Test
    void findOne() {
        Assert.assertNotEquals(null,repo.findOne("a"));
        Assert.assertNotEquals(null,repo.findOne("b"));
        Assert.assertNotEquals(null,repo.findOne("c"));
        Assert.assertEquals(null,repo.findOne("d"));
    }

    @Test
    void findAll() {
        repo.findAll();
    }

    @Test
    void save() {
        Assert.assertEquals(null, repo.save(new testItem("x")));
        Assert.assertNotEquals(null, repo.save(new testItem("a")));
    }

    @Test
    void delete() {
        Assert.assertEquals(null, repo.delete("y"));
        Assert.assertNotEquals(null, repo.delete("a"));
    }

    @Test
    void update() {
        Assert.assertEquals(null, repo.update(new testItem("a")));
        Assert.assertNotEquals(null, repo.update(new testItem("f")));
    }

    @Test
    void getValues() {
        repo.getValues();
    }

    @Test
    void getSize() {
        Assert.assertEquals(3,repo.getSize());
    }
}