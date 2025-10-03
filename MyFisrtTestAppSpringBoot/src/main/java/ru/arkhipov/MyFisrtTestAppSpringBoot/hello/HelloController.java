package ru.arkhipov.MyFisrtTestAppSpringBoot.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class HelloController {

    private ArrayList<String> stringList = null;
    private HashMap<Integer, String> stringMap = null;
    private int mapCounter = 1;

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name",
            defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/update-array")
    public String updateArrayList(@RequestParam String s) {
        if (stringList == null) {
            stringList = new ArrayList<>();
        }
        stringList.add(s);
        return "Элемент '" + s + "' добавлен в ArrayList. Текущий размер: " + stringList.size();
    }

    @GetMapping("/show-array")
    public Object showArrayList() {
        if (stringList == null || stringList.isEmpty()) {
            return "ArrayList пуст";
        }
        return stringList;
    }

    @GetMapping("/update-map")
    public String updateHashMap(@RequestParam String s) {
        if (stringMap == null) {
            stringMap = new HashMap<>();
        }
        stringMap.put(mapCounter++, s);
        return "Элемент '" + s + "' добавлен в HashMap с ключом " + (mapCounter - 1) +
                ". Текущий размер: " + stringMap.size();
    }

    @GetMapping("/show-map")
    public Object showHashMap() {
        if (stringMap == null || stringMap.isEmpty()) {
            return "HashMap пуст";
        }
        return stringMap;
    }

    @GetMapping("/show-all-length")
    public String showAllLength() {
        int listSize = (stringList != null) ? stringList.size() : 0;
        int mapSize = (stringMap != null) ? stringMap.size() : 0;

        return String.format("Количество элементов в ArrayList: %d, в HashMap: %d", listSize, mapSize);
    }
}
