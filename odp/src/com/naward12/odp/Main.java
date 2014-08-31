/**
 * Copyright 2014 SURFsara
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.naward12.odp;

import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

import com.naward12.odp.seq.ODPJob;

/**
 * Main entry point for the warcexamples. 
 * 
 * @author mathijs.kattenberg@surfsara.nl
 */
public class Main {
	public enum Programs {
		ODP("odp", "Arguments: [model] [dictionnary] [document frequency] [label index] [input seq file] [output directory]");

		private final String name;
		private final String description;

		private Programs(String name, String description) {
			this.name = name;
			this.description = description;
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}
	}

	public static void main(String[] args) {
		int retval = 0;
		boolean showUsage = false;
		if(args.length <= 0) {
			showUsage();
			System.exit(0);
		}
		String tool = args[0];
		String[] toolArgs = Arrays.copyOfRange(args, 1, args.length);
		try {
			if (Programs.ODP.getName().equals(tool)) {
				retval = ToolRunner.run(new Configuration(), new ODPJob(), toolArgs);
			}
			if (showUsage) {
				showUsage();
			}
		} catch (Exception e) {
			showErrorAndExit(e);
		}
		System.exit(retval);
	}

	private static void showErrorAndExit(Exception e) {
		System.out.println("Something didn't quite work like expected: [" + e.getMessage() + "]");
		showUsage();
		System.exit(1);
	}

	private static void showUsage() {
		System.out.println("An example program must be given as the first argument.");
		System.out.println("Valid program names are:");
		for (Programs prog : Programs.values()) {
			System.out.println(" " + prog.getName() + ": " + prog.getDescription());
		}
	}
}
