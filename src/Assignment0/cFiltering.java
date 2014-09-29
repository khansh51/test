package Assignment0;

//**********************************************************
// Assignment0:
// UTOR user_name: khansh51
//
// Author: Shameel Khan
//
//
// Honor Code: I pledge that this program represents my own
//   program code and that I have coded on my own. I received 
//   help from no one in designing and debugging my program.
//   I have also read the plagarism section in the course info
//   sheet of CSC 207 and understand the consequences.  
//*********************************************************
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Vector;

public class cFiltering {
	// this is a 2d matrix i.e. user*movie
	private int userMovieMatrix[][];
	// this is a 2d matrix i.e. user*movie
	private float userUserMatrix[][];

	/**
	 * Default Constructor
	 */
	public cFiltering() {
		// this is 2d matrix of size 1*1
		userMovieMatrix = new int[1][1];
		// this is 2d matrix of size 1*1
		userUserMatrix = new float[1][1];
	}

	// TODO:COMPLETE THIS I.E. APPROPIATELY CREATE THE USER_MOVIE_MATRIX AND
	// USER_USER_MATRIX WITH CORRECT DIMENSIONS.
	/**
	 * Constructs an object which contains two 2d matrices, one of size
	 * users*movies which will store integer movie ratings and one of size
	 * users*users which will store float similarity scores between pairs of
	 * users.
	 * 
	 * @param users
	 *            integer Determines size of matrix variables.
	 * @param movies
	 *            integer Determines size of matrix variables.
	 */
	public cFiltering(int users, int movies) {
		// this is a 2d matrix of size users*movies
		userMovieMatrix = new int[users][movies];
		// this is a 2d matrix of size users*users
		userUserMatrix = new float[users][users];
	}

	/**
	 * Reads user movie ratings from a text file, calculates similarity scores
	 * and prints a score matrix.
	 */
	public static void main(String[] args) {

		try {
			// open file to read
			String fileName;
			Scanner in = new Scanner(System.in);
			System.out.println("Enter the name of input file? ");
			fileName = in.nextLine();
			FileInputStream fstream = new FileInputStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					fstream));

			// Read dimensions: number of users and number of movies
			int numberOfUsers = Integer.parseInt(br.readLine());
			int numberOfMovies = Integer.parseInt(br.readLine());
			// create a new cfiltering object with a 2d matrix
			// i.e. user_movie_matrix with the above dimensions
			cFiltering ratingsMatrix = new cFiltering(numberOfUsers,
					numberOfMovies);

			// this is a blankline being read
			br.readLine();

			// read each line of movie ratings and populate the
			// user_movie_matrix
			String row;
			int line = 0;
			while ((row = br.readLine()) != null) {

				// allRatings is a list of all String numbers on one row
				String allRatings[] = row.split(" ");

				int column = 0;
				for (String singleRating : allRatings) {
					// make the String number into an integer
					// populate user_movie_matrix
					// TODO: COMPLETE THIS
					ratingsMatrix.userMovieMatrix[line][column] = Integer
							.parseInt(singleRating);
					column += 1;
				}
				line = line + 1;
			}
			// close the file
			fstream.close();

			// TODO:calculate similarity scores to populate the
			// user_user_matrix and print it
			// COMPLETE THIS ( I.E. CALL THE APPROPIATE FUNCTIONS THAT DOES THE
			// FOLLOWING)
			// 1.) CALCULATE THE SIMILARITY SCORE BETWEEN USERS.
			// 2.) PRINT OUT THE USER_USER_MATRIX
			// 3.) PRINT OUT THE MOST SIMILAR PAIRS OF USER AND THE MOST
			// DISSIMILAR PAIR OF USERS.

			ratingsMatrix.calculateSimilarityScore(numberOfUsers,
					numberOfMovies, ratingsMatrix);
			ratingsMatrix.printUserUserMatrix(ratingsMatrix);
			ratingsMatrix.findMostSimilarPairOfUsers(ratingsMatrix);
			ratingsMatrix.findMostDissimilarPairOfUsers(ratingsMatrix);

		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}

	// TODO:COMPLETE THIS
	// YOU ARE FREE TO CHANGE THE FUNCTION SIGNATURE BUT DO NOT CHANGE THE
	// FUNCTION NAME AND DO NOT MAKE THIS FUNCTION STATIC
	/**
	 * Determines how similar each pair of users is based on their ratings and
	 * represents this level of similarity with a float value between 0 and 1,
	 * where 1 is perfect similarity. Stores these values in the
	 * user_user_matrix.
	 */
	public void calculateSimilarityScore(int numOfUsers, int numOfMovies,
			cFiltering ratingsMatrix) {
		// Populate ratingsMatrix.userUserMatrix with the basic values of 1s,
		// that is the values of comparing a user to itself.
		for (int i = 0; i < numOfUsers; i++) {
			ratingsMatrix.userUserMatrix[i][i] = 1;
		}

		/*
		 * Calculate and store the similarityScore between all users, but do not
		 * calculated the same similarityScore twice for the same set of users,
		 * example: the similarityScore for (user1,user2)=(user2,user1). This is
		 * done to decrease run-time.
		 */

		int i = 0; // pointer1; used to loop through a user's ratings of movies
		int starter = 1;// pointer2; used to loop though a different user's
						// ratings of movies
		boolean run = true; // used to control while loop

		while (run) {
			float similarityScore;
			double underSquRoot; // the value that is under the root in the
									// distance formula
			for (int a = starter; a < numOfUsers; a++) {
				underSquRoot = 0; // reset/set to zero for comparing users with
									// users

				for (int n = 0; n < numOfMovies; n++) {
					underSquRoot += Math.pow(
							ratingsMatrix.userMovieMatrix[i][n]
									- ratingsMatrix.userMovieMatrix[a][n], 2);
				}

				double distance = Math.sqrt(underSquRoot);
				distance = (1 / (distance + 1));
				similarityScore = (float) Math.floor(10000 * distance + 0.5) 
						/ 10000;
				ratingsMatrix.userUserMatrix[i][a] = similarityScore;
				// we can reverse the users and populate the matrix for a
				// different pair of users, because (useri,userj)=(userj,useri)
				ratingsMatrix.userUserMatrix[a][i] = similarityScore;
			}
			// if we have calculated the similarty_score for all users with all
			// users, then exit while.
			if (i == numOfUsers - 1)
				run = false;
			else { // otherwise...
				i += 1; // move pointer1 to next user
				starter += 1; // move pointer2 to next user
			}

		}
	}

	// TODO:COMPLETE THIS
	// YOU ARE FREE TO CHANGE THE FUNCTION SIGNATURE BUT DO NOT CHANGE THE
	// FUNCTION NAME AND DO NOT MAKE THIS FUNCTION STATIC
	/**
	 * Prints out the similarity scores of the user_user_matrix, with each row
	 * and column representing each/single user and the cell position (i,j)
	 * representing the similarity score between user i and user j.
	 */
	public void printUserUserMatrix(cFiltering ratingsMatrix) {
		System.out.println("user_user_matrix is:");
		for (int i = 0; i < ratingsMatrix.userUserMatrix.length; i++) {
			System.out.print("[");
			for (int x = 0; x < ratingsMatrix.userUserMatrix.length; x++) {
				if (x == userUserMatrix.length - 1)
					System.out.print(ratingsMatrix.userUserMatrix[i][x] + "]");
				else
					System.out.print(ratingsMatrix.userUserMatrix[i][x] + ",");
			}
			System.out.println();
		}
		System.out.println(); // break new line
	}

	// TODO:COMPLETE THIS
	// YOU ARE FREE TO CHANGE THE FUNCTION SIGNATURE BUT DO NOT CHANGE THE
	// FUNCTION NAME AND DO NOT MAKE THIS FUNCTION STATIC
	/**
	 * This function finds the most similar pair of users in the
	 * user_user_matrix.
	 */
	public void findMostSimilarPairOfUsers(cFiltering ratingsMatrix) {

		float mostSimilarScore = 0;
		Vector vUsers = new Vector();// Stores pairs of users
		int numOfUsers = ratingsMatrix.userUserMatrix.length;
		int i = 0, starter = 1;

		for (i = 0; i < numOfUsers; i++) {
			for (int j = starter; j < numOfUsers; j++) {
				if (ratingsMatrix.userUserMatrix[i][j] > mostSimilarScore) {
					// update the mostSimilarScore
					mostSimilarScore = ratingsMatrix.userUserMatrix[i][j];
					vUsers.clear(); // clear vector for new updated pairs
					// store which pair of users have this similarity score
					vUsers.add(i + 1);
					vUsers.add(j + 1);
				} else if (ratingsMatrix.userUserMatrix[i][j] == 
						mostSimilarScore) {
					// store which pair of users have this similarity score
					vUsers.add(i + 1);
					vUsers.add(j + 1);
				}
			}
			if (starter == numOfUsers - 1)
				break;
			else
				starter += 1;
		}

		// Print out the score here. We do this because this method is a void
		// type, so it doesn't return the similarityScore

		System.out.print("The most similar pair of users from above "
				+ "user_user_matrix is:");

		i = 0;
		while (i < vUsers.size() - 1) {
			System.out.print(" User" + vUsers.get(i) + " and User"
					+ vUsers.get(i + 1) + ", ");
			i += 2; // skip to the next 'pair' of users
		}
		System.out.println("with similarity score of " + mostSimilarScore);
		System.out.println(); // break new line

	}

	// TODO:COMPLETE THIS
	// YOU ARE FREE TO CHANGE THE FUNCTION SIGNATURE BUT DO NOT CHANGE THE
	// FUNCTION NAME AND DO NOT MAKE THIS FUNCTION STATIC
	/**
	 * This function finds the most dissimilar pair of users in the
	 * user_user_matrix.
	 */
	public void findMostDissimilarPairOfUsers(cFiltering ratingsMatrix) {
		float mostDissimilarScore = 1;
		Vector vUsers = new Vector();

		int numOfUsers = ratingsMatrix.userUserMatrix.length;
		int i = 0, starter = 1;
		for (i = 0; i < numOfUsers; i++) {
			for (int j = starter; j < numOfUsers; j++) {
				if (ratingsMatrix.userUserMatrix[i][j] < mostDissimilarScore) {
					mostDissimilarScore = ratingsMatrix.userUserMatrix[i][j];
					vUsers.clear(); // clear vector for new updated pairs
					vUsers.add(i + 1);
					vUsers.add(j + 1);
				} else if (ratingsMatrix.userUserMatrix[i][j] == 
						mostDissimilarScore) {
					// if another pair of users have the same score, then add
					vUsers.add(i + 1);
					vUsers.add(j + 1);
				}
			}
			if (starter == numOfUsers - 1)
				break;
			starter += 1;
		}

		System.out.print("The most dissimilar pair of users from above "
				+ "user_user_matrix is:");

		i = 0;
		while (i < vUsers.size() - 1) {
			System.out.print(" User" + vUsers.get(i) + " and User"
					+ vUsers.get(i + 1) + ", ");
			i += 2;
		}
		System.out.println("with similarity score of " + mostDissimilarScore);

	}
}