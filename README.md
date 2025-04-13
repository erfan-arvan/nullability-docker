# Artifact for "A New Approach to Evaluating Nullability Inference Tools"

You should have received two files along with
this README:
* `paper.pdf`. This file is a copy of the accepted paper.
* `Pre-And-Post-Commits-Decision.docx`
* `nullability-comp.tar`. is a Open Container image. You can run
it using a tool like [Docker](https://www.docker.com/).

## Getting Started

1. Download `nullability-comp.tar` and navigate to the directory containing the image

   ```bash
   cd path/to/your/folder
   ```

2. Load the Docker image:

   ```bash
   docker load < nullability-comp.tar
   ```

3. Run the container interactively:

   ```bash
   ./run_docker.sh
   ```

   This will launch a shell inside the container.

4. Navigate to the workspace:

   All relevant files are located under `/opt`:

   ```bash
   cd /opt
   ```

## Directory Structure

Inside `/opt`, you will find the following folders:

- `table_1/`: Scripts and data for `Table 1`
- `table_2/`: Scripts and data for `Table 2`
- `table_3/`: Scripts and data for `Table 3`
- `manual-investigation/`: Files related to `Figure 4`
- `error-reduction/`: Files related to `Figure 5`
- `dynamic-nullability/`: Scripts for the Dynamic Nullability (section `5.2.3`)

---

---

## Table 1

This section corresponds to **Table 1** of the paper and addresses **Research Question 2 (RQ2)**:

> Do the code changes, if they exist, improve the effectiveness of nullability checkers?

To answer this, we compare the number of errors emitted by nullability checkers on the **pre-check** and **post-check** versions of each benchmark. Unlike Table 3, no inference tools are used in this experiment. We simply run the checkers on the original code before and after developers made changes for nullability verification.

All nullability annotations and `@SuppressWarnings` are removed beforehand to ensure that the results reflect the effect of **structural code changes** rather than added annotations.

> In this setup, we only run the checkers on the **raw pre-check and post-check versions** (after removing annotations), without any inferred annotations.

---

### To View Results:

```bash
./table_1.sh
```

### To Rebuild from Scratch:

```bash
./table_1.sh fresh
```

Running from scratch can take up to 1 hour. It will reprocess all benchmarks, strip annotations and suppressions, and rerun the checkers on both code versions to measure the number of emitted errors.

---

## Table 2: Manual Categorization of Code Changes

This section corresponds to **Table 2** of the paper and addresses **Research Question 1 (RQ1)**:

> When developers verify their code using a nullability checker, do they make code changes beyond adding annotations? If so, what kinds of changes do they make?

📁 **All scripts and files described below are located in** `./opt/table_2/`.

---

### Overview

To answer this question, we analyzed a set of open-source Java projects by comparing their **pre-check** (before nullability verification) and **post-check** (after verification) versions. The following steps summarize how we selected, processed, and analyzed these projects.

---

### Step-by-Step Process

1. **Select Benchmarks:**
   - We selected the projects from prior type reconstruction experiments, as described in Section 3.1.1 of the paper.
   - These benchmarks include a diverse set of real-world Java projects where developers adopted nullability checking tools.

2. **Identify Pre- and Post-Check Commits:**
   - For each benchmark, we identified two representative commits:
     - The **pre-check version**: before nullability-related annotations or suppressions were introduced.
     - The **post-check version**: after the code was updated based on nullability checker feedback.
   - When changes were spread across multiple commits, we manually selected the most representative pair.
   - All selection decisions are documented in:
     ```
     Pre-And-Post-Commits-Decision.docx
     ```
   - Further methodological details are described in **Section 3.1.1** of the paper.

3. **Count Annotations:**
   - We counted occurrences of nullability annotations and `@SuppressWarnings` in both pre-check and post-check versions.
   - Counts are saved in:
     ```
     ./results/counts/{project_name}_counts.txt
     ```

4. **Clean Code for Diffing:**
   - To focus on meaningful changes, we removed:
     - All nullability annotations
     - All `@SuppressWarnings`
     - Comments, empty lines, and import statements
   - Scripts used:
     - `removeAnnotations.py`
     - `removeCommentsLinesImports.py`

5. **Compute Diffs:**
   - Line-level diffs were generated between cleaned pre- and post-check versions using `diff`.

6. **Manual Annotation of Diffs:**
   - We manually inspected and labeled the diffs based on the type of nullability-related change.
   - Outputs:
     - Raw diffs: `./results/diffs/`
     - Annotated diffs: `./results/annotated_diffs/`

7. **Summarize Categories:**
   - Each code change was assigned a category tag representing the type of modification.
   - The final summary table is saved in:
     ```
     ./diff_categories.csv
     ```
   - To view the results interactively:
     ```bash
     python3 scripts/show_results.py
     ```

---

### How Annotation Counts Were Calculated

Each `*_counts.txt` file under `./results/counts/` provides a breakdown of nullability-related annotations before and after nullability verification.

We computed the following three metrics per project:

- **Nul**  
  Net increase in annotations that mark a value as *nullable*:
  ```text
  (Post-check @Nullable + @MonotonicNonNull)
  − (Pre-check @Nullable + @MonotonicNonNull)
  ```
  > **Note:** `@MonotonicNonNull` is treated similarly to `@Nullable` because it also indicates that a variable may be null under certain conditions.

- **Non**  
  Net increase in annotations that mark a value as *non-null*:
  ```text
  (Post-check @NonNull + @Nonnull + @NotNull + @Notnull)
  − (Pre-check @NonNull + @Nonnull + @NotNull + @Notnull)
  ```
  > **Note:** These annotations are semantically equivalent and used interchangeably across projects and tools, so we aggregate them under one category.

- **SW**  
  Net increase in nullability-related suppressions:
  ```text
  (Post-check @SuppressWarnings) − (Pre-check @SuppressWarnings)
  ```
  > **Note:** We manually filtered to include only suppressions relevant to nullability (e.g., `@SuppressWarnings("nullness")`), though the count files list all suppressions.

#### Example (`butterknife_counts.txt`)
```text
pre_check

Total annotation occurrences:
  @Nullable: 1
  @SuppressWarnings: 0

post_check

Total annotation occurrences:
  @Nullable: 17
  @SuppressWarnings: 2

Lines containing @SuppressWarnings:
  ButterKnifeProcessor.java: @SuppressWarnings("NullAway")
  FieldResourceBinding.java: @SuppressWarnings("Immutable")
```
From this, we compute:
- **Nul = 17 - 1 = 16**
- **Non = (computed from the four @NonNull variants)**
---

### Reproducing the Results

To run the full pipeline from scratch:

```bash
./table_2.sh fresh
```

This command will:

- Use pre- and post-check versions defined in the selected commits  
- Run the annotation removal and diff generation scripts  
- Count annotations and compute category summaries  
- Gather manual annotations on diff changes and count them  
- Create the same results presented in **Table 2** of the paper  

To simply view the results without rerunning everything:

```bash
./table_2.sh
python3 scripts/show_results.py
```

---

### Generated Files and Folders

After running the pipeline, the following outputs will be available inside `./opt/table_2/results/`:

- `counts/` – Contains `{project}_counts.txt` files with annotation statistics (pre vs. post)
- `diffs/` – Contains raw diffs between pre- and post-check versions (after annotation and comment removal)
- `annotated_diffs/` – Contains manually labeled diffs with change categories
- `diff_categories.csv` – A summary table with the count of changes in each category, per project

These files form the basis of **Table 2** in the paper.

---

---

## Table 3

This section corresponds to **Table 3** of the paper and addresses **Research Question 3 (RQ3)**:

> Do the code changes, if they exist, improve the effectiveness of nullability inference tools?

To answer this, we evaluate how well inference tools perform on both the **pre-check** and **post-check** versions of each benchmark, after removing all nullability annotations and suppressions. This setup allows us to isolate the effect of **code changes**, independent of manually added annotations, on the performance of inference tools.

---

### Procedure

For both the **pre-check** and **post-check** versions of each benchmark, we perform the following steps:

1. **Remove all existing nullability annotations**, including `@Nullable`, `@NonNull` variants, and `@SuppressWarnings`.
2. **Run inference tools** on the unannotated code to generate inferred annotations.
3. **Run nullability checkers** on the annotated code produced by each inference tool.
4. **Record the number of errors** emitted by each checker on the inferred code.

> 📌 The numbers reported in Table 3 represent the **number of errors** emitted by the checkers after inference—based on running the tools on cleaned (annotation-free) versions of the pre- and post-check code. A lower number indicates better inference performance.

---

### Tool and Checker Combinations

We evaluate three different **inference tools**:
- **WPI** – Whole-Program Inference
- **ANN** – Annotator (used in prior type reconstruction work)
- **NGT** – NullGTN (a graph-based inference tool)

Each inference tool is evaluated with two different **nullability checkers**:
- **CF** – The Checker Framework’s Nullness Checker
- **NW** – NullAway

This results in six inference-checker combinations per benchmark:

| Abbreviation    | Meaning                                           |
|-----------------|---------------------------------------------------|
| **WPI + CF**    | WPI inference + Checker Framework                 |
| **WPI + NW**    | WPI inference + NullAway                          |
| **ANN + CF**    | Annotator inference + Checker Framework           |
| **ANN + NW**    | Annotator inference + NullAway                    |
| **NGT + CF**    | NullGTN inference + Checker Framework             |
| **NGT + NW**    | NullGTN inference + NullAway                      |

Each combination is run on both the **pre-check** and **post-check** versions of the code, and the resulting **error counts** are reported in Table 3.

---

### To Run:

```bash
./table_3.sh
```

### To Recreate from Scratch:

```bash
./table_3.sh fresh
```

This will remove existing annotations, run all inference tools, apply both checkers to the inferred versions, and record the number of emitted **errors**—exactly as reported in **Table 3** of the paper.

---
---

## Error Reduction (Figure 5)

Contains the result of error reduction analysis:

```
ErrorReduction/NJR Benchmarks - Reduction.csv
```

---

## Manual Investigation (Figure 4)

This section presents the results of a manual investigation of the quality of annotations produced by all inference tools. It is divided into two parts. Section A reports on a comparison between tools where one marked a location as nullable while the others did not. Section B reports on a comparison between tools where one did not mark a location as nullable while the other did.

The result of Section A can be found at:
`manual-investigation/NJR Benchmarks - Annotation Analysis (1 v 2).csv`

The result of Section B can be found at:
`manual-investigation/NJR Benchmarks - Annotation Analysis (2 v 1).csv`

#### Format
Both files share the following columns:


```
kind,class,method,param,index,Path,Tool,Status,Comment
```
The first six columns uniquely identify a program location that was marked as @Nullable. For example:

```
FIELD	hobo.Player	null	name	null	url1f1de5fc71_cooijmanstim_hobo_tgz-pJ8-hobo_TestingEnviromentJ8/src/hobo/Player.java
METHOD	hobo.Player	name()	null	null	url1f1de5fc71_cooijmanstim_hobo_tgz-pJ8-hobo_TestingEnviromentJ8/src/hobo/Player.java
PARAMETER	hobo.State	equals(java.lang.Object)	o	0	url1f1de5fc71_cooijmanstim_hobo_tgz-pJ8-hobo_TestingEnviromentJ8/src/hobo/State.java
```

The `Status` column indicates whether the tool that disagreed with the others made the correct or incorrect decision, using the values `correct` or`wrong`.
The `Comment` column provides a brief justification for that judgment.
---

## Dynamic Nullability

This directory contains the scripts used to rerun the dynamic nullability experiment. The scripts execute all benchmarks using the Daikon invariant detection tool and collect every program element that became null at any point during execution. After generating these dynamic traces, we compare them against the locations inferred as `@Nullable` by the inference tools. We then compute, for each tool, the number of locations that were incorrectly left unmarked as `@Nullable`.

#### Directory Structure

- `dynamic-nullability/traces`: Contains the dynamic traces generated by Daikon for each benchmark in NJR. Each benchmark has its own directory, named accordingly, which includes two files: `trace.txt`, the raw dynamic trace output from running the benchmark with Daikon, and `serialized.txt`, which lists all locations identified as nullable based on the parsed trace.

- `dynamic-nullability/annotations`: Contains the sets of locations marked as @Nullable by each inference tool for every benchmark in NJR.

Nullable location are serialized in tab separated value format, please see examples below for the three types of location (parameter, method and field):
```
FIELD	hobo.Player	null	name	null	url1f1de5fc71_cooijmanstim_hobo_tgz-pJ8-hobo_TestingEnviromentJ8/src/hobo/Player.java
METHOD	hobo.Player	name()	null	null	url1f1de5fc71_cooijmanstim_hobo_tgz-pJ8-hobo_TestingEnviromentJ8/src/hobo/Player.java
PARAMETER	hobo.State	equals(java.lang.Object)	o	0	url1f1de5fc71_cooijmanstim_hobo_tgz-pJ8-hobo_TestingEnviromentJ8/src/hobo/State.java
```

#### Running dynamic nullability experiment

To run the dynamic nullability experiment, run the command below:
```shell
cd dynamic-nullability
python3 python3 dynamic_nullability.py
```

This script parses the cached dynamic traces to extract the detected nullable locations, and compares them against the locations inferred as `@Nullable` by each tool.


You should see output below:
```
...
processing: url74b2be4e76_gurbano_petulant_octo_sudoku_tgz-pJ8-test_testJ8
extracted nullable locations: 5
...
All nullable locations extracted.
Computing missed annotations for all tools
number of missed annotations for annotator: 17
number of missed annotations for nullgtn: 81
number of missed annotations for wpi: 3
Percentages:
annotator:94.55128205128204%
nullgtn: 74.03846153846155%
wpi: 99.03846153846155%
```

To reproduce the generated traces using daikon, run the script below:
```shell
cd dynamic-nullability
python3 python3 dynamic_nullability.py --fresh
```

You should see output below
```
...
Processing Benchmark: url70ed135da7_ralfbiedert_jcores_tgz-pJ8-sandbox_SimpleFilterTestJ8
Execting benchmark...
Executing DynComp...
Executing Chicory...
Executing Daikon...
Benchmark processed
...
```

The trace generation involves four stages of running each benchmark under different configurations.
