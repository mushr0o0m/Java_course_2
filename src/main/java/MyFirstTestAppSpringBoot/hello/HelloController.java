package MyFirstTestAppSpringBoot.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HelloController {

    private List<String> arrayList;
    private Map<Integer, String> hashMap;

    @GetMapping(value = "/hello", produces = "text/plain;charset=UTF-8")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello, " + name + "!";
    }

    @GetMapping(value = "/update-array", produces = "text/plain;charset=UTF-8")
    public String updateArrayList(@RequestParam String s) {
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        arrayList.add(s);
        return "Значение \"" + s + "\" добавлено в ArrayList. Текущий размер: " + arrayList.size();
    }

    @GetMapping("/show-array")
    public List<String> showArrayList() {
        return arrayList == null ? new ArrayList<>() : arrayList;
    }

    @GetMapping(value = "/update-map", produces = "text/plain;charset=UTF-8")
    public String updateHashMap(@RequestParam String s) {
        if (hashMap == null) {
            hashMap = new HashMap<>();
        }
        hashMap.put(hashMap.size(), s);
        return "Значение \"" + s + "\" добавлено в HashMap. Текущий размер: " + hashMap.size();
    }

    @GetMapping("/show-map")
    public Map<Integer, String> showHashMap() {
        return hashMap == null ? new HashMap<>() : hashMap;
    }

    @GetMapping(value = "/show-all-lenght", produces = "text/plain;charset=UTF-8")
    public String showAllLenght() {
        int arraySize = arrayList == null ? 0 : arrayList.size();
        int mapSize = hashMap == null ? 0 : hashMap.size();
        return "Количество элементов в ArrayList: " + arraySize + ", количество элементов в HashMap: " + mapSize;
    }
}
