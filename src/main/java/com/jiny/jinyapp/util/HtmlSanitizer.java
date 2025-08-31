package com.jiny.jinyapp.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class HtmlSanitizer {

    public static String clean(String unsafeHtml) {
        // 허용할 HTML 태그만 화이트리스트로 지정
        Safelist safelist = Safelist.relaxed(); // <b>, <i>, <a>, <p>, <ul>, <table> 등 기본 지원
        safelist.addTags("h1", "h2", "pre", "code"); // 추가로 허용할 태그들
        return Jsoup.clean(unsafeHtml, safelist);
    }
}
