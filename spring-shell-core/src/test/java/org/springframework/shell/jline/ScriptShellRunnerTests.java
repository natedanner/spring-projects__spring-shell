/*
 * Copyright 2024 the original author or authors.
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
package org.springframework.shell.jline;

import org.junit.jupiter.api.Test;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

import static org.assertj.core.api.Assertions.assertThat;

public class ScriptShellRunnerTests {

    private final ScriptShellRunner runner = new ScriptShellRunner(null, null);

	@Test
	void shouldNotRunWhenNoArgs() {
		assertThat(runner.canRun(of())).isFalse();
	}

	@Test
	void shouldNotRunWhenInOptionValue() {
		assertThat(runner.canRun(of("--foo", "@"))).isFalse();
	}

	@Test
	void shouldNotRunWhenJustFirstArgWithoutFile() {
		assertThat(runner.canRun(of("@"))).isFalse();
	}

	@Test
	void shouldRunWhenFirstArgHavingFile() {
		assertThat(runner.canRun(of("@file"))).isTrue();
	}

	private static ApplicationArguments of(String... args) {
		return new DefaultApplicationArguments(args);
	}
}
