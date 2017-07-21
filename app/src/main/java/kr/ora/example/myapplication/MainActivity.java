package kr.ora.example.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    Handler handler;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        webView = (WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        // 구글홈페이지 지정
        String html = "<html>\n" +
                "    <head>\n" +
                "        <title>Hello</title>\n" +
                "    </head>\n" +
                "\n" +
                "    <script>\n" +
                "\n" +
                "        function hello(msg) {\n" +
                "\n" +
                "            this.document.frmHello.inName.value =msg;\n" +
                "        }\n" +
                "        function hello2()\n" +
                "        {\n" +
                "            alert(this.document.frmHello.inName.value);\n" +
                "            window.HybridApp.setMessage(this.document.frmHello.inName.value);\n" +
                "\n" +
                "        }\n" +
                "    </script>\n" +
                "\n" +
                "    <body>\n" +
                "\n" +
                "    Hello <hr>\n" +
                "    <form name=\"frmHello\">\n" +
                "        <input name=\"inName\" type=text> <input type=\"button\" value=\"OK\" onClick=\"hello();\">\n" +
                "        <input type=\"button\" value=\"send\" onClick=\"hello2();\" >\n" +
                "    </form>\n" +
                "\n" +
                "    <a href=\"http://www.google.com\"> click here !</a>    </body>\n" +
                "</html>";
        webView.loadData(html,"text/html",null);
//        webView.loadUrl("http://oracletest.run.goorm.io/samplehybrid/");
        // WebViewClient 지정
        webView.setWebViewClient(new WebViewClientClass());
        editText = (EditText)findViewById(R.id.editText);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                webView.loadUrl("javascript://alert('"+editText.getText().toString()+"')");
//                webView.loadUrl("javascript:hello('"+editText.getText().toString()+"');");
                String url="";
                WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();
                if (mWebBackForwardList.getCurrentIndex() > 0)
                    url= mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex()-1).getUrl();
//                webView.loadUrl(url);
                webView.goBack();
            }
        });
        webView.addJavascriptInterface(new AndroidBridge(), "HybridApp");
        startActivity(new Intent(this,Main2Activity.class));
    }

    private class WebViewClientClass extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            Log.d("loadurl",url);
            return true;
        }
    }

    private class AndroidBridge {

        @android.webkit.JavascriptInterface
        public void setMessage(final String arg) { // must be final
            handler.post(new Runnable() {
                public void run() {
                    Log.d("HybridApp", "setMessage("+arg+")");
                    Toast.makeText(MainActivity.this,arg,Toast.LENGTH_SHORT).show();
//                    mTextView.setText(arg);
                }
            });
        }
    }

}
