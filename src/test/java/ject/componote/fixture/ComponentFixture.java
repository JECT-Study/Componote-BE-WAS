package ject.componote.fixture;

import ject.componote.domain.common.model.BaseImage;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.component.domain.ComponentType;
import ject.componote.domain.component.domain.MixedName;
import ject.componote.domain.component.domain.summary.ComponentSummary;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public enum ComponentFixture {
    BASIC_COMPONENT(ComponentType.INPUT, List.of("Title1", "Title2"), "Basic Summary"),
    ADVANCED_COMPONENT(ComponentType.FEEDBACK, List.of("Title3"), "Advanced Summary");

    private final ComponentType type;
    private final List<String> mixedNames;
    private final String summaryText;

    ComponentFixture(final ComponentType type, final List<String> mixedNames, final String summaryText) {
        this.type = type;
        this.mixedNames = mixedNames;
        this.summaryText = summaryText;
    }

    public Component 컴포넌트_생성(Long id) {
        List<MixedName> mixedNameList = mixedNames.stream()
                .map(MixedName::from)
                .collect(Collectors.toList());
        ComponentSummary summary = ComponentSummary.of("test", "test", BaseImage.from("test"));
        Component component = Component.of(type, mixedNameList, summary);

        // 리플렉션이나 별도의 메서드를 이용해 ID 설정
        setField(component, "id", id);
        return component;
    }

    // 리플렉션을 활용해 ID를 주입하는 메서드
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }



    public ComponentType getType() {
        return type;
    }

    public List<String> getMixedNames() {
        return mixedNames;
    }

    public String getSummaryText() {
        return summaryText;
    }
}

