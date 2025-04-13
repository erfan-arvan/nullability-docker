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

## Table 1

To view results:

- Quick run:

  ```bash
  ./table_1.sh
  ```

- Rebuild from scratch:

  ```bash
  ./table_1.sh fresh
  ```

  This can take up to 1 hour.

The script runs nullability checkers on both the pre- and post-check versions before applying any inference tools. As stated in the paper, all nullability annotations and `@SuppressWarnings` have been removed to isolate meaningful code changes.

---

## Table 2: Manual Categorization of Code Changes

This part of the artifact corresponds to **Table 2** of the paper and addresses **Research Question 1 (RQ1)**:

> When developers verify their code using a nullability checker, do they make code changes beyond adding annotations? If so, what kinds of changes do they make?

### Procedure

Here are the steps we followed:

1. **Count Annotations:**
   - Counted the occurrences of nullability annotations (`@Nullable`, `@NonNull`, etc.) and `@SuppressWarnings` in both pre- and post-check versions.
   - Counts are available in:  
     ```
     ./results/counts/{project_name}_counts.txt
     ```

2. **Annotation and Noise Removal:**
   - Removed all nullability annotations and `@SuppressWarnings`.
   - Also removed comments, empty lines, and import statements.
   - Scripts used:
     - `removeAnnotations.py`
     - `removeCommentsLinesImports.py`

3. **Diff Generation:**
   - Computed line-level diffs between the cleaned pre- and post-check versions using `diff`.

4. **Manual Categorization:**
   - Authors manually reviewed the diffs and categorized nullability-related changes.
   - Annotated diffs are saved in:
     ```
     ./results/annotated_diffs/
     ```
   - Raw diffs are stored in:
     ```
     ./results/diffs/
     ```

5. **Category Summary:**
   - We summarized the number of changes per category for each project in:
     ```
     ./diff_categories.csv
     ```
   - For a human-readable version, run:
     ```bash
     python3 scripts/show_results.py
     ```

### Categories of Code Changes

Each change was labeled with a category tag representing the type of nullability-related code modification. The categories and their descriptions can be seen when running the result visualization script.

The summary file `./diff_categories.csv` aggregates the number of changes per category per project, and this data is used to produce Table 2 in the paper.

### Annotation Count Interpretation

Each `*_counts.txt` file under `./results/counts/` includes pre- and post-check counts of the following:

- `@Nullable`
- `@NonNull`, `@Nonnull`, `@NotNull`, `@Notnull` (treated as variations of NonNull)
- `@MonotonicNonNull`
- `@SuppressWarnings`

#### Example: `butterknife_counts.txt`

```text
pre_check

Total annotation occurrences:
  @Nullable: 1
  ...
  @SuppressWarnings: 0

post_check

Total annotation occurrences:
  @Nullable: 17
  ...
  @SuppressWarnings: 2

Lines containing @SuppressWarnings:
  ButterKnifeProcessor.java: @SuppressWarnings("NullAway")
  FieldResourceBinding.java: @SuppressWarnings("Immutable")
```

### How to Calculate Table 2 Metrics

For each project:

- **Nul** = post-check `@Nullable` count - pre-check `@Nullable` count  
  Example: `17 - 1 = 16` for ButterKnife
- **Non** = Sum of added `@NonNull`, `@Nonnull`, `@NotNull`, `@Notnull`
- **SW** = Increase in nullability-related `@SuppressWarnings`  
  Note: We manually reviewed and counted only nullability-related suppressions (e.g., `@SuppressWarnings("nullness")`), even though the total shown in the count files includes all types.

### Reproducing the Results

To generate all results from scratch:

```bash
./table_2.sh fresh
```

This will:

- Clone all benchmarks
- Generate pre- and post-check versions
- Run annotation removal, diff computation, and counting scripts

### Commit Selection

For each project, we selected commits representing:

- **Pre-check version:** The state of the project before nullability verification
- **Post-check version:** The state after nullability verification

In most cases, the commit just before nullability-related changes was chosen as the pre-check version. When changes were spread across multiple commits, we manually selected the best-matching commits and documented our decisions in:

```
Pre-And-Post-Commits-Decisions.docx
```

Further details are also in Section 3.1.1 of the paper.

### Files Summary

- `./results/diffs/` – Raw diffs between pre- and post-check versions  
- `./results/annotated_diffs/` – Manually annotated diffs with category labels  
- `./results/counts/` – Annotation and `@SuppressWarnings` statistics  
- `./diff_categories.csv` – Summary of categorized changes used in Table 2  
- `scripts/show_results.py` – Tool to view results in a structured, readable format

### To Quickly View the Results

```bash
./table_2.sh
python3 scripts/show_results.py
```

---

## Table 3

This section evaluates the performance of inference tools and their generated annotations, corresponding to Table 3 of the paper.

### To Run:

```bash
./table_3.sh
```

### To Recreate from Scratch:

```bash
./table_3.sh fresh
```

Like Table 1, annotations and `@SuppressWarnings` are removed before running inference tools. Then, nullability checkers are executed on the inferred code.

---

## Error Reduction (Figure 5)

Contains the result of error reduction analysis:

```
ErrorReduction/NJR Benchmarks - Reduction.csv
```

---

## Manual Investigation (Figure 4)

Manual annotation comparison results are provided in two CSV files:

```
ManualInvestigation/NJR Benchmarks - Annotation Analysis (1 v 2).csv
ManualInvestigation/NJR Benchmarks - Annotation Analysis (2 v 1).csv
```

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
METHOD	hobo.Player	toStr
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