package numbers;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Stream;

public class Main {
    static Scanner s = new Scanner(System.in);
    static String[] err = {
            "The first parameter should be a natural number or zero.\n",
            "The second parameter should be a natural number.\n",
            "Available properties:" +
                    "[EVEN, ODD, BUZZ, DUCK, PALINDROMIC, GAPFUL," +
                    " SPY, SQUARE, SUNNY, JUMPING, HAPPY, SAD]\n",
            "There are no numbers with these properties.\n"
    };

    public static void main(String[] args) {
        System.out.println("Welcome to Amazing Numbers!");
        System.out.println();
        System.out.println("Supported requests:\n" +
                "- enter a natural number to know its properties;\n" +
                "- enter two natural numbers to obtain the properties of the list:\n" +
                "  * the first parameter represents a starting number;\n" +
                "  * the second parameter shows how many consecutive numbers are to be printed;\n" +
                "- two natural numbers and properties to search for;\n" +
                "- a property preceded by minus must not be present in numbers;\n" +
                "- separate the parameters with one space;\n" +
                "- enter 0 to exit.\n");

        while (true) {
            try {
                System.out.print("Enter a request: ");
                String[] n = s.nextLine().trim().split("\\s+");
                System.out.println();

                long p1 = Long.parseLong(n[0]);
                if (p1 == 0) break;
                if (p1 < 0) {
                    error(0);
                    continue;
                }

                if (n.length == 1) {
                    boolean isEven = isEven(p1);
                    boolean isHappy = isHappy(p1);

                    System.out.printf("Properties of %s\n" +
                                    "        buzz: %b\n" +
                                    "        duck: %b\n" +
                                    " palindromic: %b\n" +
                                    "      gapful: %b\n" +
                                    "         spy: %b\n" +
                                    "      square: %b\n" +
                                    "       sunny: %b\n" +
                                    "     jumping: %b\n" +
                                    "        even: %b\n" +
                                    "         odd: %b\n" +
                                    "       happy: %b\n" +
                                    "         sad: %b\n\n",
                            NumberFormat.getInstance().format(p1),
                            isBuzz(p1), isDuck(p1), isPalindromic(p1),
                            isGapful(p1), isSpy(p1), isSquare(p1), isSunny(p1),
                            isJumping(p1), isEven, !isEven, isHappy, !isHappy);
                } else {
                    long p2 = Long.parseLong(n[1]);
                    if (p2 < 1) {
                        error(1);
                        continue;
                    }

                    if (n.length == 2) {
                        Stream.iterate(p1, x -> x < p1 + p2, x -> x + 1)
                                .forEach(x -> System.out.print(getDescription(x, getAttributes(x))));
                        System.out.println();
                    } else {
                        List<String> paras = List.of(
                                "BUZZ", "DUCK", "PALINDROMIC", "GAPFUL", "SPY", "SQUARE",
                                "SUNNY", "JUMPING", "EVEN", "ODD", "HAPPY", "SAD",
                                "-BUZZ", "-DUCK", "-PALINDROMIC", "-GAPFUL", "-SPY", "-SQUARE",
                                "-SUNNY", "-JUMPING", "-EVEN", "-ODD", "-HAPPY", "-SAD");
                        List<String> terms = new ArrayList<>();
                        List<String> errTerms = new ArrayList<>();
                        for (int i = 2; i < n.length; i++) {
                            if (paras.contains(n[i].toUpperCase())) {
                                terms.add(n[i].toLowerCase());  // get the parameters
                            } else {
                                errTerms.add(n[i].toUpperCase());
                            }
                        }

                        if (!errTerms.isEmpty()) {
                            if (errTerms.size() == 1) {
                                System.out.printf("The property %s is wrong.\n",
                                        Arrays.toString(errTerms.toArray()));
                            } else {
                                System.out.printf("The properties %s are wrong.\n",
                                        Arrays.toString(errTerms.toArray()));
                            }
                            error(2);
                            continue;
                        }
                        if (!isInclusive(terms)) continue;

                        long temp = p1;
                        long counter = p2;
                        while (counter > 0) {
                            String[] attr = getAttributes(temp);
                            boolean isValid = true;
                            for (String term : terms) {
                                if (term.startsWith("-")) {
                                    isValid = !List.of(attr).contains(term.substring(1));
                                } else {
                                    isValid = List.of(attr).contains(term);
                                }

                                if (!isValid) {
                                    break;
                                }
                            }

                            if (isValid) {
                                counter--;
                                System.out.print(getDescription(temp, attr));
                            }
                            temp += 1;
                        }
                        System.out.println();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
//                error(0);
            }
        }
        System.out.println("Goodbye!");
    }

    static void error(int i) {
        System.out.println(err[i]);
    }

    static boolean isBuzz(long n) {
        String str = String.valueOf(n);
        return n % 7 == 0 || str.endsWith("7");
    }

    static boolean isDuck(long n) {
        String str = String.valueOf(n);
        return str.contains("0");
    }

    static boolean isPalindromic(long n) {
        String str = String.valueOf(n);
        boolean isPalindromic = true;
        for (int i = 0; i < str.length() / 2; i++) {
            if (str.toCharArray()[i]
                    != str.toCharArray()[str.length() - i - 1]) {
                isPalindromic = false;
                break;
            }
        }
        return isPalindromic;
    }

    static boolean isGapful(long n) {
        String str = String.valueOf(n);
        return str.length() > 2
                && n % (long) ((str.charAt(0) - '0') * 10
                + (str.charAt(str.length() - 1) - '0')) == 0;
    }

    static boolean isSpy(long n) {
        String str = String.valueOf(n);
        long sum = 0;
        long product = 1;

        for (char c : str.toCharArray()) {
            int i = c - '0';
            sum += i;
            product *= i;
        }
        return sum == product;
    }

    static boolean isSquare(long n) {
        double root = Math.sqrt(n);
        root = root - root % 1;  // round to integral part
        return root * root == n;
    }

    static boolean isSunny(long n) {
        return isSquare(n + 1);
    }

    static boolean isEven(long n) {
        return n % 2 == 0;
    }

    static boolean isJumping(long n) {
        while (n != 0) {
            long curr = n % 10;
            n /= 10;
            if (n != 0) {
                long next = n % 10;
                if (Math.abs(curr - next) != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean isHappy(long n) {
        Set<Integer> history = new HashSet<>();

        long temp = n;
        while (true) {
            char[] chars = String.valueOf(temp).toCharArray();

            int sum = 0;
            for (char c : chars) {
                int value = c - '0';
                sum += value * value;
            }

            if (sum == 1) {
                return true;  // if the product is 1, then it's happy
            } else if (history.contains(sum)) {
                return false;
            }

            history.add(sum);
            temp = sum;  // continue the loop
        }
    }

    static String[] getAttributes(long n) {
        boolean isEven = isEven(n);
        boolean isHappy = isHappy(n);

        return ((isEven ? "even " : "")
                + (!isEven ? "odd " : "")
                + (isBuzz(n) ? "buzz " : "")
                + (isDuck(n) ? "duck " : "")
                + (isPalindromic(n) ? "palindromic " : "")
                + (isGapful(n) ? "gapful " : "")
                + (isSpy(n) ? "spy " : "")
                + (isSquare(n) ? "square " : "")
                + (isSunny(n) ? "sunny " : "")
                + (isJumping(n) ? "jumping " : "")
                + (isHappy ? "happy " : "")
                + (!isHappy ? "sad" : ""))
                .trim()
                .split("\\s+");
    }

    static String getDescription(long n, String[] attr) {
        StringBuilder description = new StringBuilder(attr[0]);
        if (attr.length > 1) {
            for (int i = 1; i < attr.length; i++) {
                description.append(", ").append(attr[i]);
            }
        }
        return String.format("%15s is %s\n", NumberFormat.getInstance().format(n), description);
    }

    static boolean isInclusive(List<String> terms) {
        String err = "The request contains mutually exclusive properties: ";
        for (String term : terms) {
            if (terms.contains(term) && terms.contains("-" + term)) {
                System.out.println(err + "[-" + term.toUpperCase() + ", " + term.toUpperCase() + "]");
                error(3);
                return false;
            }
        }

        if (terms.contains("even") && terms.contains("odd")) {
            System.out.println(err + "[ODD, EVEN]");
        } else if (terms.contains("duck") && terms.contains("spy")) {
            System.out.println(err + "[DUCK, SPY]");
        } else if (terms.contains("square") && terms.contains("sunny")) {
            System.out.println(err + "[SQUARE, SUNNY]");
        } else if (terms.contains("happy") && terms.contains("sad")) {
            System.out.println(err + "[HAPPY, SAD]");
        } else if (terms.contains("-even") && terms.contains("-odd")) {
            System.out.println(err + "[-ODD, -EVEN]");
        } else if (terms.contains("-happy") && terms.contains("-sad")) {
            System.out.println(err + "[-HAPPY, -SAD]");
        } else {
            return true;
        }
        error(3);
        return false;
    }
}
