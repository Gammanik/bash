# Library for arguments parsing


Here is the comparison of libraries I have considered for parsing command lines arguments:

| Name | Last release date |  API | Configurable Option Prefix | Strongly Typed Option Parameters | Configurable Validation | Multi-value Options | Subcommands | License
|----------|:-------------:|------:|------:|------:|------:|------:|------:|------:|
| [jcommander](http://jcommander.org/) | 2017-05-17 | annotations & reflection | yes | yes | yes | yes | yes | Apache 2.0 |
| [argparse4j](https://github.com/tatsuhiro-t/argparse4j) | 2017-09-18 | Builder | yes | yes| no | yes| yes | MIT
| [args4j](http://args4j.kohsuke.org/) | 2016-01-31 | annotations & reflection | yes | yes | no | yes | yes | MIT
| [cli-parser]( https://github.com/spullara/cli-parser) | 2016-01-31 | annotations & reflection | yes | yes | no | yes | yes | MIT 
| [picocli](http://picocli.info) |    2019-12-08   | annotations reflection, and builder API| yes | yes | minimal | yes | yes | Apache 2.0


Eventually, I have chosen [jcommander](http://jcommander.org/) mostly because of user-friendly documentation web-cite with [`kotlin`](http://jcommander.org/#_kotlin) section in it.
