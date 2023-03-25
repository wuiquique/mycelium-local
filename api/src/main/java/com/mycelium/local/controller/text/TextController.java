package com.mycelium.local.controller.text;

import java.util.HashMap;
import java.util.Map;

import com.mycelium.local.repository.text.TextRepo;
import com.mycelium.local.repository.text.TranslationText;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

class TextUpdateRequest {
    public String component;
    public String key;
    public String value;
}

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/text")
public class TextController {
    private TextRepo textRepo;

    public TextController(TextRepo textRepo) {
        this.textRepo = textRepo;
    }

    @Get("/")
    public Map<String, Map<String, String>> list() {
        var compmap = new HashMap<String, Map<String, String>>();
        for (var text : textRepo.findAll()) {
            if (!compmap.containsKey(text.component)) {
                compmap.put(text.component, new HashMap<String, String>());
            }
            var keymap = compmap.get(text.component);
            keymap.put(text.key, text.value);
        }
        return compmap;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Put("/")
    public void set(@Body TextUpdateRequest body) {
        var textOpt = textRepo.findByComponentAndKey(body.component.toLowerCase(), body.key.toLowerCase());
        TranslationText text;
        if (textOpt.isPresent()) {
            text = textOpt.get();
        } else {
            text = new TranslationText();
            text.component = body.component.toLowerCase();
            text.key = body.key.toLowerCase();
        }
        text.value = body.value;
        if (textOpt.isPresent()) {
            textRepo.update(text);
        } else {
            textRepo.save(text);
        }
    }
}
