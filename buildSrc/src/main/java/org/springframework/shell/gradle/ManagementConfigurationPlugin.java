/*
 * Copyright 2022-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.shell.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaTestFixturesPlugin;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;

/**
 * Creates a Management configuration that is appropriate for adding a platform
 * to that is not exposed externally. If the JavaPlugin is applied, the
 * compileClasspath, runtimeClasspath, testCompileClasspath, and
 * testRuntimeClasspath will extend from it.
 *
 * @author Janne Valkealahti
 */
public class ManagementConfigurationPlugin implements Plugin<Project> {

	public static final String MANAGEMENT_CONFIGURATION_NAME = "management";

	@Override
	public void apply(Project project) {
		ConfigurationContainer configurations = project.getConfigurations();
		configurations.create(MANAGEMENT_CONFIGURATION_NAME, (management) -> {
			management.setVisible(false);
			management.setCanBeConsumed(false);
			management.setCanBeResolved(false);
			PluginContainer plugins = project.getPlugins();
			plugins.withType(JavaPlugin.class, (javaPlugin) -> {
				configurations.getByName(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME).extendsFrom(management);
				configurations.getByName(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME).extendsFrom(management);
			});
			plugins.withType(JavaTestFixturesPlugin.class, (javaTestFixturesPlugin) -> {
				configurations.getByName("testFixturesCompileClasspath").extendsFrom(management);
				configurations.getByName("testFixturesRuntimeClasspath").extendsFrom(management);
			});
			plugins.withType(OptionalDependenciesPlugin.class, (optionalDependencies) -> configurations
				.getByName(OptionalDependenciesPlugin.OPTIONAL_CONFIGURATION_NAME).extendsFrom(management));
			plugins.withType(MavenPublishPlugin.class, (mavenPublish) -> {
				PublishingExtension publishing = project.getExtensions().getByType(PublishingExtension.class);
				publishing.getPublications().withType(MavenPublication.class, (mavenPublication -> {
					mavenPublication.versionMapping((versions) ->
							versions.allVariants(versionMapping -> versionMapping.fromResolutionResult())
					);
				}));
			});
		});
	}
}
