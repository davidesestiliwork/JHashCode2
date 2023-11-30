# JHashCode2

The program generate checksums of file and folders in GNU-style.

New in release 2:
- Use relative path instead of absolute path
- Can exclude symbolic links
- Can exclude hidden files
- Can sign file with GnuPG (if installed and key pair exists)
- Can exclude multiple folders

Usage: param 1: exclude symbolic links (0 or 1) [mandatory], param 2: exclude hidden files (0 or 1) [mandatory], param 3: folders to exclude [optional]

Example: java -jar JHashCode2.jar 1 1 /home/davide/snap,/home/davide/Video,/home/davide/vmware
