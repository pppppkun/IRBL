package pgd.irbl.business;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pgd.irbl.business.po.RepoCommit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BusinessApplication {

    public static void main(String[] args) throws IOException {
//        SpringApplication.run(BusinessApplication.class, args);
    }
}
