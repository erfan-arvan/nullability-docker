def count_occurrences(filename, search_string):
    count = 0
    try:
        with open(filename, 'r') as file:
            for line in file:
                count += line.count(search_string)
    except FileNotFoundError:
        print(f"File {filename} not found.")
    return count

def main():
    search_string = '[nullness:'
    
    # Count occurrences in preCheck.out
    pre_check_count = count_occurrences('preCheck.out', search_string)
    
    # Count occurrences in postCheck.out
    post_check_count = count_occurrences('postCheck.out', search_string)
    
    print(f'Occurrences of "{search_string}" in preCheck.out: {pre_check_count}')
    print(f'Occurrences of "{search_string}" in postCheck.out: {post_check_count}')

if __name__ == "__main__":
    main()

