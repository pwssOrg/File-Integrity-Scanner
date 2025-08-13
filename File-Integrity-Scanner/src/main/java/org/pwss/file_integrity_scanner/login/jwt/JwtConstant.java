package org.pwss.file_integrity_scanner.login.jwt;

 final class JwtConstant {

      /**
     * Holds the instance count. The maximum instances allowed is 1.
     */
    private static int instanceCount = 0;

    //TODO: generate an unique secret each time you start the app 
     private final String SECRET = "rgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergrgergergergergergergergergergv";

     private final long EXPIRATION_TIME = 900_000;
     private final String TOKEN_PREFIX = "Bearer ";
     private final String HEADER_STRING = "Authorization";

     private final String ORGANIZATION="PWSS_ORG";
     private final String CLAIM ="username";

     private final String SUBJECT="User Details";


    JwtConstant() {

        instanceCount++;
        if (instanceCount > 1) {
            System.exit(2);
            // It is only allowed to have one instance of this class

        } else {

            // debug log that class is Initilizaed 
        }
    }

    final String getSECRET() {
         return SECRET;
     }

      final long getEXPIRATION_TIME() {
         return EXPIRATION_TIME;
     }

    final String getTOKEN_PREFIX() {
         return TOKEN_PREFIX;
     }

    final String getHEADER_STRING() {
         return HEADER_STRING;
     }

    final String getORGANIZATION() {
         return ORGANIZATION;
     }

    final String getCLAIM() {
         return CLAIM;
     }

    final String getSUBJECT() {
         return SUBJECT;
     }
}