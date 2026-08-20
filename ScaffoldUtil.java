import java.io.File;
import java.io.FileWriter;

public class ScaffoldUtil
{
    public static void scaffoldProject(String template)
    {
        File dir = new File(template);
        if (dir.exists())
        {
            System.out.println("Directory already exists!");
            return;
        }
        
        dir.mkdir();
        
        try
        {
            if (template.equals("python-api"))
            {
                new File(dir, "main.py").createNewFile();
                new File(dir, "requirements.txt").createNewFile();
                
                FileWriter fw = new FileWriter(new File(dir, "main.py"));
                fw.write("from fastapi import FastAPI\n\napp = FastAPI()\n\n@app.get('/')\ndef read_root():\n    return {'status': 'active'}");
                fw.close();
                
                FileWriter rw = new FileWriter(new File(dir, "requirements.txt"));
                rw.write("fastapi\nuvicorn");
                rw.close();
                
                System.out.println("✔ Python API scaffolded in /" + template);
            }
            else if (template.equals("web"))
            {
                new File(dir, "index.html").createNewFile();
                new File(dir, "style.css").createNewFile();
                new File(dir, "script.js").createNewFile();
                
                FileWriter fw = new FileWriter(new File(dir, "index.html"));
                fw.write("<!DOCTYPE html>\n<html>\n<head>\n<link rel=\"stylesheet\" href=\"style.css\">\n</head>\n<body>\n<h1>Forge Scaffold</h1>\n<script src=\"script.js\"></script>\n</body>\n</html>");
                fw.close();
                
                System.out.println("✔ Web project scaffolded in /" + template);
            }
            else if (template.equals("data-science"))
            {
                new File(dir, "data").mkdir();
                new File(dir, "notebooks").mkdir();
                new File(dir, "src").mkdir();
                new File(dir, "config").mkdir();
                new File(dir, "requirements.txt").createNewFile();
                
                FileWriter rw = new FileWriter(new File(dir, "requirements.txt"));
                rw.write("pandas\nnumpy\nscikit-learn\nmatplotlib\njupyter\nsqlalchemy\npsycopg2-binary\nxgboost\ntensorflow");
                rw.close();
                
                new File(dir, "config/.env").createNewFile();
                FileWriter cw = new FileWriter(new File(dir, "config/.env"));
                cw.write("DB_HOST=localhost\nDB_USER=root\nDB_PASS=password\nDB_NAME=ml_db");
                cw.close();
                
                new File(dir, "src/db_pipeline.py").createNewFile();
                FileWriter dw = new FileWriter(new File(dir, "src/db_pipeline.py"));
                dw.write("import os\nimport pandas as pd\nfrom sqlalchemy import create_engine\n\ndef get_engine():\n    user = os.getenv('DB_USER')\n    password = os.getenv('DB_PASS')\n    host = os.getenv('DB_HOST')\n    db = os.getenv('DB_NAME')\n    return create_engine(f'postgresql://{user}:{password}@{host}/{db}')\n\ndef load_data(query):\n    engine = get_engine()\n    return pd.read_sql(query, engine)");
                dw.close();
                
                System.out.println("✔ Advanced Data Science environment scaffolded in /" + template);
            }
            else if (template.equals("comp-prog"))
            {
                new File(dir, "tests").mkdir();
                new File(dir, "input.txt").createNewFile();
                new File(dir, "output.txt").createNewFile();
                new File(dir, "solution.cpp").createNewFile();
                
                FileWriter cw = new FileWriter(new File(dir, "solution.cpp"));
                cw.write("#include <iostream>\n#include <vector>\n#include <algorithm>\nusing namespace std;\nvoid solve(){\n    \n}\nint main(){\n    ios_base::sync_with_stdio(false);\n    cin.tie(NULL);\n    #ifndef ONLINE_JUDGE\n    freopen(\"input.txt\", \"r\", stdin);\n    freopen(\"output.txt\", \"w\", stdout);\n    #endif\n    int t = 1;\n    cin >> t;\n    while(t--){\n        solve();\n    }\n    return 0;\n}");
                cw.close();
                
                System.out.println("✔ Competitive Programming environment scaffolded in /" + template);
            }
            else
            {
                System.out.println("Unknown template. Try 'python-api', 'web', 'data-science', or 'comp-prog'.");
                dir.delete();
            }
        }
        catch (Exception e)
        {
            System.err.println("Error scaffolding: " + e.getMessage());
        }
    }
}