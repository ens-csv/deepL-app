package com.example.translationapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TranslationController {

    @Value("${deepl.api.key}")
    private String deeplApiKey;

    @PostMapping("/translate")
    public List<String> translateText(@RequestBody TranslateRequest request) {
        String[] sentences = request.getText().split("\\. ");
        List<String> result = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();
        String deeplUrl = "https://api-free.deepl.com/v2/translate";

        for (String sentence : sentences) {
            String trimmedSentence = sentence.trim();
            String url = String.format("%s?auth_key=%s&text=%s&target_lang=%s", deeplUrl, deeplApiKey, trimmedSentence, request.getTargetLanguage());
            DeepLResponse response = restTemplate.getForObject(url, DeepLResponse.class);

            if (response != null && !response.getTranslations().isEmpty()) {
                result.add(response.getTranslations().get(0).getText());
                result.add(trimmedSentence);
            }
        }

        return result;
    }
}
