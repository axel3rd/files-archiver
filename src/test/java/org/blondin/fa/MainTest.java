package org.blondin.fa;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.blondin.fa.io.ZipTools;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * Test class
 */
public class MainTest extends TestCase {

    /**
     * Setup
     * 
     * @throws Exception Problem
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        FileUtils.deleteDirectory(new File("target/dataTest"));
        FileUtils.copyDirectory(new File("dataTest"), new File("target/dataTest"));
        new File("target/dataTest/out", "nothing.file").getParentFile().mkdirs();
    }

    /**
     * Test main
     * 
     * @throws Exception If problem
     */
    @Test
    public void testMain() throws Exception {
        // Create "__thumb" in input and output directory, will be deleted
        File thumbIn = new File("target/dataTest/in/__thumb", "test.jpg");
        thumbIn.getParentFile().mkdirs();
        thumbIn.createNewFile();
        File thumbOut = new File("target/dataTest/out/__thumb", "test.jpg");
        thumbOut.getParentFile().mkdirs();
        thumbOut.createNewFile();

        Main.main(new String[] { "target/dataTest/in", "target/dataTest/out", "jpg,jpeg", "thumb,cdslkj,.svn" });

        // Simple check
        assertTrue(new File("target/dataTest/out/car", "car.zip").exists());
        assertTrue(new File("target/dataTest/out/car/accident", "accident.zip").exists());
        assertTrue(new File("target/dataTest/out/lamp", "lamp.zip").exists());
        assertTrue(new File("target/dataTest/out/me", "me.zip").exists());

        // Must not exist, forbidden path
        assertFalse(new File("target/dataTest/out/__thumb", "test.jpg").exists());

        // Gif must not be compressed
        assertEquals(Integer.parseInt("8"), ZipTools.getNumberOfFiles(new File("target/dataTest/out/me", "me.zip")));
    }

    /**
     * Test forbidden
     * 
     * @throws Exception If problem
     */
    @Test
    public void testForbidden() throws Exception {
        Main.main(new String[] { "target/dataTest/in", "target/dataTest/out", "jpg,jpeg", "thumb,cdslkj,.svn" });
        assertTrue(new File("target/dataTest/out/car", "car.zip").exists());
        Main.main(new String[] { "target/dataTest/in", "target/dataTest/out", "jpg,jpeg", "thumb,cdslkj,.svn,car" });
        assertFalse(new File("target/dataTest/out/car", "car.zip").exists());
    }

    /**
     * Test Orphan
     * 
     * @throws Exception If problem
     */
    @Test
    public void testOrphan() throws Exception {
        Main.main(new String[] { "target/dataTest/in", "target/dataTest/out", "jpg,jpeg", "thumb,cdslkj,.svn" });
        new File("target/dataTest/out/car/orphan", "nothing.file").getParentFile().mkdirs();
        Main.main(new String[] { "target/dataTest/in", "target/dataTest/out", "jpg,jpeg", "thumb,cdslkj,.svn" });
        assertFalse(new File("target/dataTest/out/car/orphan").exists());
    }

    /**
     * Test number file change
     * 
     * @throws Exception If problem
     */
    @Test
    public void testNumberFileChange() throws Exception {
        // First time with gif
        Main.main(new String[] { "target/dataTest/in", "target/dataTest/out", "jpg,jpeg,gif", "thumb,cdslkj,.svn" });
        assertEquals(Integer.parseInt("9"), ZipTools.getNumberOfFiles(new File("target/dataTest/out/me", "me.zip")));

        // Second without Gif => recompress
        Main.main(new String[] { "target/dataTest/in", "target/dataTest/out", "jpg,jpeg", "thumb,cdslkj,.svn" });
        assertEquals(Integer.parseInt("8"), ZipTools.getNumberOfFiles(new File("target/dataTest/out/me", "me.zip")));
    }

    /**
     * Test Clean ZIP
     * 
     * @throws Exception If problem
     */
    @Test
    public void testCleanZip() throws Exception {
        Main.main(new String[] { "target/dataTest/in", "target/dataTest/out", "jpg,jpeg", "thumb,cdslkj,.svn" });
        new File("target/dataTest/out/car", "car2.zip").createNewFile();

        Main.main(new String[] { "target/dataTest/in", "target/dataTest/out", "jpg,jpeg", "thumb,cdslkj,.svn" });
        assertFalse(new File("target/dataTest/out/car", "car2.zip").exists());
    }
}
