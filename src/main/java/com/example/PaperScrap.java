package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PaperScrap {
    public static List<String> searchForPdf(String query) throws IOException {
        List<String> pdfUrls = new ArrayList<>();

        String url = "https://www.google.com/search?q=" + query + "+filetype:pdf";

        try {
            // Mengambil halaman dengan Jsoup
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();

            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String href = link.attr("href");
                if (href.endsWith(".pdf")) {
                    pdfUrls.add(href);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;  // Mengembalikan null jika terjadi kesalahan
        }

        return pdfUrls;
    }
}
