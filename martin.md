# Artifact for "Static Program Reduction via Type-Directed Slicing"

You should have received two files along with
this README:
* `issta25-preprint.pdf`. This file is a copy of the accepted paper.
* `specimin.tar`. is a Open Container image. You can run
it using a tool like [Docker](https://www.docker.com/).

This document gives instructions for how to use `specimin.tar` to
reproduce the claims in `issta25-preprint.pdf`.

## Requirements

These instructions are written assuming a Linux environment on a machine
with an x86-64 architecture. Don't try to run these experiments on a modern
MacBook (with an arm chip) inside the Docker image; that will make the performance
abysmally slow. The only software other than a Linux machine that you should
need is Docker (and a PDF reader, to view the preprint).

## Getting Started

To begin, run the following command in a directory containing `specimin.tar`:
```
docker run -it specimin /bin/bash
```

This command should give you a shell within the container. Take a look
around with `ls` and make sure you see at least the following items
(among others):
* a directory called `specimin`. This is the tool's implementation.
("specimin" is the public name of the tool, although the preprint
calls the tool "TypeSlice" for anonymity. Our camera-ready will update
the name.) This directory contains the version of the tool that we used
in the experiments that we report on in the paper. You can also find
the most recent version of specimin at https://github.com/njit-jerse/specimin.
You are more than welcome to explore the tool's implementation; see its
`README` file for details about e.g., how to build it and run its tests.
* a script called `rq1.sh`. This script runs the experiments that
we report on in the big table in the paper (Table 1).
* a script called `rq4.sh`. This script runs the experiments that compare
specimin to Perses and its variants, which we report on in section 6.5
of the paper. Please see section "rq4" below about this script.
* a script called `rq5.sh`. This script runs the experiment that we report
on in section 6.6.

Note that RQs 2 and 3 don't need their own scripts, since the answers to them
are derived from the RQ1 resutls; see the explanation in the detailed instructions below.

To kick the tires on the artifact, you will use the `rq1-fast.sh` script to run Specimin
on a single issue from Table 1: CF-4614 (which is the smallest issue). Running this evaluation
should take less than a minute on most machines. The last thing that the script will print
is a table like the following:
```
issue_name    |    status    |  Fail reason  | preservation_status | preservation reason
------------------------------------------------------------------------------------------
(1)cf-4614    |    PASS     |                | PASS           |
```

Check that it looks right (i.e., both `status` and `preservation_status` are `PASS`); if so, you've successfully kicked the tires.

## Detailed Description

### RQ1: Can Specimin preserve the behavior of real Java typecheckers on a historical dataset of typechecker bugs?

Run the `rq1.sh` script to generate
the main results in Table 1. Run the following command:
```
./rq1.sh
```

It should take an hour or two on most machines to run specimin's approximate mode on the set of 28 bugs
from the table (the docker container slows it down quite a bit, unfortunately; the most expensive part
is running the analysis tools to confirm that Specimin gets the right results).
When it's finished, it should print an ASCII version of the table
to the terminal (after quite a bit of intermediate output, most of which you can ignore;
this intermediate output may include seemingly-concerning messages like "gradle build failed",
but that's fine--the scripts are intentionally triggering failures in the subject analysis tools).
A copy of the script's output is saved to the file `rq1.log` (in the same directory as `rq1.sh`), also.
The output at the end should look something like this:
```
issue_name    |    status    |  Fail reason  | preservation_status | preservation reason 
------------------------------------------------------------------------------------------
(1)cf-1291    |    PASS     |                | PASS           |   
(2)cf-6282    |    PASS     |                | PASS           |   
...
```

That "status" column indicates whether specimin successfully minimized the target program
(i.e., did not crash). The "preservation_status" column indicates whether or not rerunning
the target typechecker on specimin's output matches the issue signature. A "PASS" in
that column corresponds to a checkmark in the column labeled "Pr.?" in table 1; a "FAIL" corresponds to one of the other
symbols.

Note that the table produced by the script includes some issues that we excluded from the paper.
The file `rq1/specimin-evaluation/exclusions.md` contains the explanation for why each of the
following issues was excluded:
* cf-3025
* na-89
* na-791b ("na-791a" is the one that the paper refers to as "NA-791")

The rest of this section covers:
* low-level details of how the evaluation system works, so that you can modify it if you'd like
* "=" vs "X" in the "Pr.?" column of Table 1

#### Low-level details of the evaluation system

We use this script as part of our continuous integration setup for the main specimin repo
at https://github.com/njit-jerse/specimin. The core of the infrastructure is a set of Python
scripts that:
* check out each target project
* run Specimin on each project
* run the typechecker whose behavior is to be preserved on Specimin's output
* and then check that the signature (e.g., the stack trace) matches what we expect

These scripts are in the `specimin-evaluation` directory at the top-level of the provided
Docker image. They're also open-source here: https://github.com/njit-jerse/specimin-evaluation/.
See its `README.md` file for a description of its contents.

The main input to these scripts is the file `resources/test_data.json`. It contains
an entry for each bug; adding a new bug to the evaluation involves adding an entry
to this file.

You can run the evaluation script on just one of the bugs by using the
`--debug` flag. For example, to only run the NA-97 bug's eval, run:
`python3 main.py --debug na-97`.

Once a bug has been run through the evaluation script, both a copy of the original
repository and Specimin's output are found in the corresponding folder of the
`ISSUES` folder under `specimin-evaluation`. If you want to see Specimin's output
for a specific issue, you can do so here. For example, if you want to validate
our claims about why Specimin fails to reproduce certain bugs, you could look at the
associated entry in the `ISSUES` folder.

These evaluation scripts support more issues than we report on in the paper. The file
`exclusions.md` under `specimin-evaluation` lists each bug that our scripts support
but which we didn't include in Table 1, along with the reason that we excluded it.

#### "=" vs "X" in the "Pr.?" column of Table 1

Table 1 uses three symbols in the column about preservation:
* a checkmark, for successful preservation in approximate mode
* an "=", to indicate failure to preserve in approximate mode but success in exact mode
* an "X", to indicate failure in both modes

Our evaluation script only runs approximate mode for most bugs: if approximate mode works,
than exact mode is guaranteed to work. The entry in the table for NA-705, however, is an
"=". The evaluation script checks NA-705 in both modes, using two different runs (meaning
that there are two rows in the ASCII table produced by `./rq1.sh` for NA-705):
the row labeled "NA-705" is approximate mode, and the row "NA-705-jar" is exact mode.

### RQ2: Is Specimin’s running time acceptable?

We answered RQ2 by recording the time that it takes to run the RQ1 scripts on our
CI server, which is a free-tier GitHub actions runner. For example, you can see a
recent CI run's log here: https://productionresultssa13.blob.core.windows.net/actions-results/31d628a7-df59-47ec-9107-f34385f7ede7/workflow-job-run-265237e6-5512-52e4-8a4b-de47208a8db8/logs/job/job-logs.txt?rsct=text%2Fplain&se=2025-04-08T17%3A36%3A31Z&sig=Sny8Snq2tzujGTqzWWxuVXlbBFMdy1VnrcbbrV%2FVl24%3D&ske=2025-04-09T03%3A05%3A49Z&skoid=ca7593d4-ee42-46cd-af88-8b886a2f84eb&sks=b&skt=2025-04-08T15%3A05%3A49Z&sktid=398a6654-997b-47e9-b12b-9515b896b4de&skv=2025-01-05&sp=r&spr=https&sr=b&st=2025-04-08T17%3A26%3A26Z&sv=2025-01-05

If you search this log for "Avg runtime", you'll find a json blob with the number of seconds that it
took for Specimin to minimize each of issues in the CI run. The timing numbers in Table 1 came
from the most recent CI run at the time of submission.
These numbers are also printed locally when you run `rq1.sh`, just above the ASCII table of output;
however, the we've noticed that the Docker container is a bit slower than our CI builds,
even on powerful machines.

### RQ3: How close is the output of Specimin to what a human developer would do by hand?

We mostly answered this RQ by inspecting the output of Specimin and the corresponding human-written
tests by hand. After running `./rq1.sh`, Specimin's outputs for each bug are in the directory
`rq1/specimin-evaluation/ISSUES/${ISSUE_ID}/output/` (where `${ISSUE_ID}` is the id of the issue in
`resources/test_data.json` (e.g., `cf-1291`).
The human-written test cases have been collected
in the directory `rq1/specimin-evaluation/resources/human-written-tests` (also arranged by issue id).

We measured the size of both Specimin's outputs and the human-written tests using [scc](https://github.com/boyter/scc).

### RQ4: How does TypeSlice compare to Perses [7], Vulcan [20], and T-Rec [21], state-of-the-art
dynamic program reduction tools?

Running `./rq4.sh` will find every instance of subscripts that run individual
tests on Perses and its variants.

Each completed experiment will be inside the `output` subfolder inside it's
issue. Each of these will have a shell script that can be run to replicate the
bug it is meant to preserve.
For example, `CF-4614` will produce `Version.java` & `testcommand.sh`
in `cf-4614/output/src/main/java/net/mtu/eggplant/checker/parser_error/`

RQ4 should take about 15 hours to run in total.

To run any individual experiment, cd into any of the issue folders and run `runthis.sh`.
The directory structure is:
- rq4
  - perses-evaluation
    - perses-vanilla # experiments with "vanilla" Perses, without Vulcan or T-Rec (P columns in table 2)
    - perses-trec # T-Rec experiments (TR columns in table 2)
    - perses-vulcan # Vulcan experiments (V columns in table 2)
    - specimin-into-perses # experiments with Specimin and Perses (TS+P columns in table 2)

Each of these folders has one subdirectory corresponding to each issue (i.e., row in table 2),
except for cases with a n/a or an X in the corresponding entry in the table.

### RQ5: Is Specimin applicable to a broad range of programs?

Running `rq5.sh` will run the entire experiment for the results in section
6.6.2. To determine if specimin was applicable to more programs, we sampled
popular repositories and ran specimin on every method in their source code.
This experiment takes a very long time to run (about 4 days) and requires 40GB
of combined memory and swap. The results of the experiment will be written to
`/home/gradle/rq5/ASHE_Automated-Software-Hardening-for-Entrypoints/logs` 
with the success rate in each repository and a ranking of the most common
failure reasons when a method's reduction does not succeed.

The final result from our run of this experiment is located at
`/home/gradle/rq5/our_results` should you not run the experiment locally.

The script `rq5-fast.sh` runs the experiment on a singular repository. To run
the experiment on a subset of repositories, simply copy lines from
`/home/gradle/rq5/docker_list.csv` to `/home/gradle/rq5/fast_list.csv` and run
`rq5-fast.sh`. It will read the latter file for repositories instead.