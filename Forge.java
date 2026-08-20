import java.sql.*;
import java.io.File;
import java.io.FileWriter;

public class Forge
{
    private static Connection connect() throws SQLException{

    String userHome = System.getProperty("user.home");
    String dbPath = userHome + File.separator + "forge.db";
    return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
}

    private static void initDB()
    {
        String sql = "CREATE TABLE IF NOT EXISTS snippets (id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT UNIQUE NOT NULL,content TEXT NOT NULL);";
        try (Connection conn = connect(); Statement smt = conn.createStatement())
        {
            smt.execute(sql);
        }
        catch (SQLException e)
        {
            System.err.println("Database error:" + e.getMessage());
        }
    }

    private static void saveSnippet(String title, String content)
    {
        String sql = "INSERT OR REPLACE INTO snippets (title,content) VALUES (?,?)";
        try (Connection conn = connect(); PreparedStatement psmt = conn.prepareStatement(sql))
        {
            psmt.setString(1, title);
            psmt.setString(2, content);
            psmt.executeUpdate();
            System.out.println("saved Snippet " + title + " to database!");
        }
        catch (SQLException e)
        {
            System.err.println("Error saving:" + e.getMessage());
        }
    }

    private static void getSnippet(String title)
    {
        String sql = "SELECT content FROM snippets WHERE title=?";
        try (Connection conn = connect(); PreparedStatement psmt = conn.prepareStatement(sql))
        {
            psmt.setString(1, title);
            ResultSet rs = psmt.executeQuery();
            if (rs.next())
            {
                String code = rs.getString("content");
                ClipboardUtil.copyToClipboard(code);
                System.out.println("Copied Snippet " + title + " to Clipboard");
            }
            else
            {
                System.out.println("Snippet " + title + " Not found");
            }
        }
        catch (SQLException e)
        {
            System.err.println("Error retreiving" + e.getMessage());
        }
    }

    private static void listSnippets()
    {
        String sql = "SELECT title FROM snippets";
        try (Connection conn = connect(); Statement smt = conn.createStatement(); ResultSet rs = smt.executeQuery(sql))
        {
            System.out.println("----List of saved snippets----");
            while (rs.next())
            {
                System.out.println("- " + rs.getString("title"));
            }
            System.out.println("---------------------\n");
        }
        catch (SQLException e)
        {
            System.err.println("ERROR LISTING" + e.getMessage());
        }
    }

    private static void deleteSnippet(String title)
    {
        String sql = "DELETE FROM snippets WHERE title=?";
        try (Connection conn = connect(); PreparedStatement psmt = conn.prepareStatement(sql))
        {
            psmt.setString(1, title);
            int affectedRows = psmt.executeUpdate();
            if (affectedRows > 0)
            {
                System.out.println("Deleted Snippet " + title + " successfully");
            }
            else
            {
                System.out.println("Snippet " + title + " not found");
            }
        }
        catch (SQLException e)
        {
            System.err.println("Error deleting " + e.getMessage());
        }
    }

    public static void main(String[] args)
    {
        initDB();
        if (args.length < 1)
        {
            System.out.println("\n--- FORGE CLI ---");
            System.out.println("To Save:  forge save <title> <code>");
            System.out.println("To Get:   forge get <title>");
            System.out.println("To Gen:   forge gen <title> <prompt>");
            System.out.println("To List:   forge list");
            System.out.println("To Delete: forge delete <title>\n");
            return;
        }
        
        String action = args[0].toLowerCase();
        
        if (action.equals("list"))
        {
            listSnippets();
            return;
        }
        
        if (args.length < 2)
        {
            System.out.println("Missing arguements for command");
            return;
        }
        
        String title = args[1];
        
        if (action.equals("save") && args.length >= 3)
        {
            StringBuilder contentBuilder = new StringBuilder();
            for (int i = 2; i < args.length; i++)
            {
                contentBuilder.append(args[i]);
                if (i < args.length - 1)
                {
                    contentBuilder.append(" ");
                }
            }
            String content = contentBuilder.toString();
            saveSnippet(title, content);
        }
        else if (action.equals("get"))
        {
            getSnippet(title);
        }
        else if (action.equals("gen") || action.equals("ai"))
        {
            System.out.println("generating code using AI");
            String prompt = args[2];
            String Aigen = AiService.generateCode(prompt);
            Aigen = Aigen.replace("\\u003c", "<").replace("\\u003e", ">"); 
            ClipboardUtil.copyToClipboard(Aigen);
            saveSnippet(title, Aigen);
            System.out.println("AI generated code successfully");
        }
        else if (action.equals("delete"))
        {
            deleteSnippet(title);
        }
        else if (action.equals("init") && args.length >= 2)
        {
            String template = args[1];
            ScaffoldUtil.scaffoldProject(template);
        }
        else
        {
            System.out.println("Invaild Command");
        }
    }
}