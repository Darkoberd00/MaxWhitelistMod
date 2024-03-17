package dev.philtraeger.db;

import dev.philtraeger.MaxWhitelist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static final Logger logger = LogManager.getLogger(MaxWhitelist.class);
    private static Database instance;
    static Connection conn = null;
    private Database() {
        createNewDatabase();
        createTable();
        try {
            logger.info(conn.getMetaData().getDriverName());
        } catch (SQLException e) {
            logger.error(e.getStackTrace());
        }

    }

    public static Database getInstance(){
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Connect to a sample database
     */
    private static void createNewDatabase() {

        String currentDir = System.getProperty("user.dir");
        String url = "jdbc:sqlite:" + currentDir + "/whitelist.db";

        try {
            conn = DriverManager.getConnection(url);
            if (conn == null) return;

            DatabaseMetaData meta = conn.getMetaData();
            logger.info("The driver name is " + meta.getDriverName());
            logger.info("A new database has been created.");
        } catch (SQLException e) {
            logger.error(e.getStackTrace());
        }
    }

    private static void createTable() {
        try {
            if (conn == null) return;

            String sql = "CREATE TABLE IF NOT EXISTS users ("
                    + "	discord_id varchar(255) NOT NULL,"
                    + "	username_minecraft varchar(255) NOT NULL,"
                    + "	uuid_minecraft varchar(255) NOT NULL,"
                    + "	wildcard boolean DEFAULT false"
                    + ");";
            Statement stmt = conn.createStatement();
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            logger.error(e.getStackTrace());
        }
    }

    public void addUser(String discordID, String usernameMinecraft, String uuidMinecraft, boolean wildcard) {
        String sql = "INSERT INTO users(discord_id,username_minecraft,uuid_minecraft,wildcard) VALUES(?,?,?,?);";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return;
            pstmt.setString(1, discordID);
            pstmt.setString(2, usernameMinecraft);
            pstmt.setString(3, uuidMinecraft);
            pstmt.setBoolean(4, wildcard);
            pstmt.executeUpdate();
        }  catch (SQLException e) {
            logger.error(e.getStackTrace());
        }

    }

    public void removeUserViaUsername(String username){
        String sql = "DELETE FROM users WHERE username_minecraft LIKE ?;";
        try {
            if (conn == null) return;
            PreparedStatement stmt  = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getStackTrace());
        }
    }

    public void removeUserViaDiscordID(String discordID){
        String sql = "DELETE FROM users WHERE uuid_minecraft LIKE ?;";
        try {
            if (conn == null) return;
            PreparedStatement stmt  = conn.prepareStatement(sql);
            stmt.setString(1, discordID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getStackTrace());
        }
    }

    public boolean isInDB(String discordID){
        String sql = "SELECT discord_id FROM users WHERE discord_id LIKE ?;";
        try {
            if (conn == null) return false;
            PreparedStatement stmt  = conn.prepareStatement(sql);
            stmt.setString(1, discordID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (!rs.getString("discord_id").isEmpty()) return true;
            }
        } catch (SQLException e) {
            logger.error(e.getStackTrace());
        }
        return false;
    }

    public boolean isInDBMinecraftUserame(String minecraftUsername){
        String sql = "SELECT username_minecraft FROM users WHERE username_minecraft LIKE ?;";
        try {
            if (conn == null) return false;
            PreparedStatement stmt  = conn.prepareStatement(sql);
            stmt.setString(1, minecraftUsername);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (!rs.getString("username_minecraft").isEmpty()) return true;
            }
        } catch (SQLException e) {
            logger.error(e.getStackTrace());
        }
        return false;
    }

    public ArrayList<DBUser> getAllUser() {
        ArrayList<DBUser> users = new ArrayList<>();
        String sql = "SELECT discord_id, username_minecraft, uuid_minecraft, wildcard FROM users;";
        try {
            if (conn == null) return users;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                DBUser user = new DBUser();
                user.discordID = rs.getString("discord_id");
                user.usernameMinecraft = rs.getString("username_minecraft");
                user.uuidMinecraft = rs.getString("uuid_minecraft");
                user.wildcard = rs.getBoolean("wildcard");
                users.add(user);
            }
        } catch (SQLException e) {
            logger.error(e.getStackTrace());
        }
        return users;
    }

    public DBUser getUser(String username) {
        String sql = "SELECT discord_id, username_minecraft, uuid_minecraft, wildcard FROM users WHERE username_minecraft LIKE ?;";
        try {
            if (conn == null) return null;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            DBUser user = new DBUser();
            user.discordID = rs.getString("discord_id");
            user.usernameMinecraft = rs.getString("username_minecraft");
            user.uuidMinecraft = rs.getString("uuid_minecraft");
            user.wildcard = rs.getBoolean("wildcard");
            return user;
        } catch (SQLException e) {
            logger.error(e.getStackTrace());
        }
        return null;
    }
}
