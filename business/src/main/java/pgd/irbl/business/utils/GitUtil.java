package pgd.irbl.business.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author: pkun
 * @CreateTime: 2021-06-18 09:05
 */
@Component
@Slf4j
public class GitUtil {

    @Value("${repo_direction}")
    private String repoDirection;

    public void copyAndReset(String source, String destination, String commitId) {
        try {
            Process process = Runtime.getRuntime().exec("./reset.sh " + repoDirection + destination + " " + commitId + " " + repoDirection + source);
            InputStream inputStream = process.getInputStream();
            process.waitFor();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                bufferedReader.lines().forEach(log::info);
            }
            process.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteRepo(String gitUrl) {
        try {
            Process process = Runtime.getRuntime().exec("rm -rf " + getRegisterRepoPath(gitUrl));
            process.waitFor();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String showFileSpecificCommit(String gitUrl, String commitId, String filePath) {
        try {
            Process process = Runtime.getRuntime().exec("./getFile.sh " + getRegisterRepoPath(gitUrl) + " " + commitId + " " + filePath);
            InputStream inputStream = process.getInputStream();
            process.waitFor();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                bufferedReader.lines().forEach(log::info);
            }
            process.destroy();
            Path path = Paths.get(repoDirection + "result.txt");
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRegisterRepoPath(String gitUrl) {
        String repoName = gitUrl.substring(gitUrl.lastIndexOf("/") + 1, gitUrl.lastIndexOf(".git"));
        return repoDirection + repoName + gitUrl.hashCode();
    }


    public String getRegisterRepoGitPath(String gitUrl) {
        return getRegisterRepoPath(gitUrl) + "/.git";
    }

}
