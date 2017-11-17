package care.solve.blockchain;

import care.solve.fabric.FabricSdk;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackageClasses = {
        BlockchainAdapterApp.class, FabricSdk.class
},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public class BlockchainAdapterApp {
    public static void main( String[] args ) {
        SpringApplication.run(BlockchainAdapterApp.class, args);
    }
}
