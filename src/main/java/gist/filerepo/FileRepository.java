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
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;

public class FileRepository {

    static {
        //logger (disable jgit logging)
        LoggerContext loggerFactory = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerFactory.getLogger("org.eclipse.jgit").setLevel(Level.OFF);
    }

    public static final File getFile(String path) throws IOException {
        return (File) getData(path, null, false);
    }

    public static byte[] getBytes(String path) throws IOException {
        return (byte[]) getData(path, null, true);
    }

    public static void load(OutputStream out, String path) throws IOException {
        getData(path, out, false);
    }

    private static Object getData(String path, OutputStream out, Boolean returnBytes) throws IOException {
        Object ret = null;
        Git git = Git.open(Paths.get(".").toFile());
        Repository repository = git.getRepository();
        ObjectId lastCommitId = repository.resolve(Constants.HEAD);

        boolean found = false;
        try (RevWalk revWalk = new RevWalk(repository)) {
            RevCommit commit= revWalk.parseCommit(lastCommitId);
            RevTree tree = commit.getTree();
            System.out.println("Having tree:" + tree);
            try (TreeWalk treeWalk = new TreeWalk(repository)) {
                treeWalk.addTree(tree);
                treeWalk.setRecursive(true);
                treeWalk.setFilter(PathFilter.create(path));
                while (treeWalk.next()) {
                    found = true;
                    ret = new File(treeWalk.getPathString());
                    ObjectId objectId = treeWalk.getObjectId(0);
                    ObjectLoader loader = repository.open(objectId);
                    if (out != null) {
                        loader.copyTo(out);
                        break;
                    }
                    if (returnBytes) ret = loader.getBytes();
                }
                if (!found) {
                    new FileNotFoundException(path);
                }
            }
            revWalk.dispose();;
        }

        return ret;
    }
}
