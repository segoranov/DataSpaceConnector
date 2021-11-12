package org.eclipse.dataspaceconnector.spi.types.domain.asset;

import org.eclipse.dataspaceconnector.spi.types.TypeManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.dataspaceconnector.common.testfixtures.TestUtils.getResourceFileContentAsString;

class AssetTest {

    private TypeManager typeManager;

    @BeforeEach
    void setUp() {
        typeManager = new TypeManager();
    }

    @Test
    void verifySerialization() {
        var asset = Asset.Builder.newInstance().id("abcd123")
                .contentType("application/json")
                .version("1.0")
                .name("testasset")
                .property("some-critical.value", 21347)
                .build();

        var json = typeManager.writeValueAsString(asset);
        assertThat(json).isNotNull();

        assertThat(json).contains("abcd123")
                .contains("application/json")
                .contains("testasset")
                .contains("some-critical.value")
                .contains("21347")
                .contains("1.0");
    }

    @Test
    void verifyDeserialization() {
        var json = getResourceFileContentAsString("serialized_asset.json");
        var asset = typeManager.readValue(json, Asset.class);

        assertThat(asset).isNotNull();
        assertThat(asset.getId()).isEqualTo("abcd123");
        assertThat(asset.getContentType()).isEqualTo("application/json");
        assertThat(asset.getName()).isNull();
        assertThat(asset.getProperty("numberVal")).isInstanceOf(Integer.class).isEqualTo(42069);

        assertThat(asset.getProperties()).hasSize(5);

    }

    @Test
    void getProperty_whenNotPresent_shouldReturnNull() {
        var asset = Asset.Builder.newInstance().build();
        assertThat(asset.getProperty("notexist")).isNull();
    }

    @Test
    void getNamedProperty_whenNotPresent_shouldReturnNull() {
        var asset = Asset.Builder.newInstance().build();
        assertThat(asset.getName()).isNull();
        assertThat(asset.getVersion()).isNull();
    }
}