package hospitalmanagement.haresh.com.rssfeedexample;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Haresh on 21-Jan-18.
 */

public class ReadRss extends AsyncTask<Void,Void,Void> {
    ArrayList<FeedItem> feeditems;
    String address = "https://vijaykarnataka.indiatimes.com/rssfeedsdefault.cms";
    Context context;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    URL url;

    public ReadRss(Context context,RecyclerView recyclerView) {
        this.recyclerView=recyclerView;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        MyAdapter myAdapter=new MyAdapter(feeditems,context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalSpace(50));
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Processxml(getdata());
        return null;
    }

    private void Processxml(Document data) {
        if (data != null) {
            feeditems=new ArrayList<>();
            Element root=data.getDocumentElement();
            Node channel=root.getChildNodes().item(0);
            NodeList items=channel.getChildNodes();
            for (int i=0;i<items.getLength();i++){
                Node currentchild=items.item(i);
                if (currentchild.getNodeName().equalsIgnoreCase("item")){
                    FeedItem item=new FeedItem();
                    NodeList itemchilds=currentchild.getChildNodes();
                    for (int j=0;j<itemchilds.getLength();j++){
                        Node current=itemchilds.item(j);
                        if (current.getNodeName().equalsIgnoreCase("title"))
                        {
                            item.setTitle(current.getTextContent());
                        }
                        else if (current.getNodeName().equalsIgnoreCase("link"))
                        {
                            item.setLink(current.getTextContent());
                        }
                        else if (current.getNodeName().equalsIgnoreCase("pubDate"))
                        {
                            item.setPubDate(current.getTextContent());
                        }
                        else if (current.getNodeName().equalsIgnoreCase("image"))
                        {
                            item.setThumbnailUrl(current.getTextContent());
                        }

                    }feeditems.add(item);
                }
            }
        }
    }

    public Document getdata() {
        try {
            url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDoc = builder.parse(inputStream);
            return xmlDoc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}