package gui.tests;

import gui.AppBoard;
import orgObjects.Category;
import orgObjects.Question;

import java.io.File;

class ZipLocalizerTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        ZipLocalizer myZippy=new ZipLocalizer();
        myZippy.source=new File("dummydata/06082022e.zip");
        myZippy.destination=new File("dummydata/unload");
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void openLocalizedBoard() {
        File myPath =new File("dummydata\\1.txt");
        AppBoard.createContents(AppBoard.parseBoard(myPath),myPath);
    }

    @org.junit.jupiter.api.Test
    void fixPaths() {
        File myPath =new File("dummydata\\fixed_06162022a\\quiz.txt");
        ZipLocalizer myZippy=new ZipLocalizer();
        myZippy.pathPrefix="C:\\";
        Category[] cats=myZippy.fixPaths(myPath);
        for (Category c:cats
             ) {
            System.out.println("CATEGORY: "+c.getName());
            for (Question q:c.getQuestions()
                 ) {
                System.out.println("Question "+q.getLevel());
                System.out.println(q.getQuestion());
                System.out.println(q.getAnswer());

            }

        }

    }
}