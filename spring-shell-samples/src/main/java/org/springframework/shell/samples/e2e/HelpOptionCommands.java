/*
 * Copyright 2022 the original author or authors.
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
package org.springframework.shell.samples.e2e;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class HelpOptionCommands extends BaseE2ECommands {

	@Autowired
	Supplier<CommandRegistration.Builder> builder;

	@ShellMethod(key = LEGACY_ANNO + "help-option-default", group = GROUP)
	public String testHelpOptionDefault(
		@ShellOption(defaultValue = "hi") String arg1
	) {
		return "Hello " + arg1;
	}

	@Bean
	public CommandRegistration testHelpOptionDefaultRegistration() {
		return builder.get()
			.command(REG, "help-option-default")
			.group(GROUP)
			.withOption()
				.longNames("arg1")
				.defaultValue("hi")
				.and()
			.withTarget()
				.function(ctx -> {
					String arg1 = ctx.getOptionValue("arg1");
					return "Hello " + arg1;
				})
				.and()
			.build();
	}

	// @ShellMethod(key = LEGACY_ANNO + "help-option-exists", group = GROUP)
	// public String testHelpOptionExists(
	// 	@ShellOption(defaultValue = "hi") String help
	// ) {
	// 	return "Hello " + help;
	// }

	@Bean
	public CommandRegistration testHelpOptionExistsRegistration() {
		return builder.get()
			.command(REG, "help-option-exists")
			.group(GROUP)
			.withOption()
				.longNames("help")
				.defaultValue("hi")
				.and()
			.withHelpOptions()
				.longNames("myhelp")
				.and()
			.withTarget()
				.function(ctx -> {
					String arg1 = ctx.getOptionValue("help");
					return "Hello " + arg1;
				})
				.and()
			.build();
	}
}
