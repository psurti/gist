package gist.filerepo;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class FileRepositoryTest {

    @ParameterizedTest
    @ValueSource(strings = {"modules/git.gradle", "modules/jackson.gradle","README.md"})
    @Disabled
    public void testGit(String file) throws IOException  {

        System.out.println("###############"+ file  +"###############");
        //logger (disable jgit logging)
        LoggerContext loggerFactory = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerFactory.getLogger("org.eclipse.jgit").setLevel(Level.OFF);

        System.out.println("Looking for " + file);
        Git git = Git.open(Paths.get(".").toFile());
        Repository repository = git.getRepository();
        ObjectId lastCommitId = repository.resolve(Constants.HEAD);

        try (RevWalk revWalk = new RevWalk(repository)) {
            RevCommit commit= revWalk.parseCommit(lastCommitId);
            RevTree tree = commit.getTree();
            System.out.println("Having tree:" + tree);
            try (TreeWalk treeWalk = new TreeWalk(repository)) {
                treeWalk.addTree(tree);
                treeWalk.setRecursive(true);
                treeWalk.setFilter(PathFilter.create(file));
                while (treeWalk.next()) {
                    System.out.println("found: " + treeWalk.getPathString());
                    System.out.println("name-string:" + new File(treeWalk.getPathString()));
                    System.out.println( "abs-path:"+ new File(treeWalk.getPathString()).length());
                    ObjectId objectId = treeWalk.getObjectId(0);
                    ObjectLoader loader = repository.open(objectId);
                    loader.copyTo(System.out);
                }

            }
            revWalk.dispose();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"modules/spring.gradle"})
    void getFile(String filepath) throws IOException {
        File file = FileRepository.getFile(filepath);
        System.out.println("Returned:" + file.getAbsolutePath());
        Assertions.assertEquals("spring.gradle", file.getName(), "file not found");
    }

    @ParameterizedTest
    @ValueSource(strings = {"modules/spring.gradle"})
    void getBytes(String filepath) throws IOException {
        byte[] bytes = FileRepository.getBytes(filepath);
        System.out.println(new String(bytes));
        Assertions.assertNotNull(bytes, new String(bytes));
    }

    @ParameterizedTest
    @ValueSource(strings = {"modules/spring.gradle"})
    void load(String filepath) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        FileRepository.load(out, filepath);
        out.close();
        String data = out.toString(StandardCharsets.UTF_8);
        System.out.println(data);
        Assertions.assertNotNull(data, data);

    }
}