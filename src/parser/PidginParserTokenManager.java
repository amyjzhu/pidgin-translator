/* PidginParserTokenManager.java */
/* Generated By:JavaCC: Do not edit this line. PidginParserTokenManager.java */
package parser;
import java.util.ArrayList;
import java.util.List;
import parser.parsetree.*;
import util.*;
import java.util.Collections;

/** Token Manager. */
@SuppressWarnings("unused")public class PidginParserTokenManager implements PidginParserConstants {

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0){
   switch (pos)
   {
      case 0:
         if ((active0 & 0x18007fff000L) != 0L)
         {
            jjmatchedKind = 42;
            return 2;
         }
         return -1;
      case 1:
         if ((active0 & 0x8006002000L) != 0L)
            return 2;
         if ((active0 & 0x10001ffd000L) != 0L)
         {
            if (jjmatchedPos != 1)
            {
               jjmatchedKind = 42;
               jjmatchedPos = 1;
            }
            return 2;
         }
         return -1;
      case 2:
         if ((active0 & 0x10001bf0000L) != 0L)
         {
            if (jjmatchedPos != 2)
            {
               jjmatchedKind = 42;
               jjmatchedPos = 2;
            }
            return 2;
         }
         if ((active0 & 0x800200d000L) != 0L)
            return 2;
         if ((active0 & 0x400000L) != 0L)
         {
            if (jjmatchedPos < 1)
            {
               jjmatchedKind = 42;
               jjmatchedPos = 1;
            }
            return -1;
         }
         return -1;
      case 3:
         if ((active0 & 0x10003bf0000L) != 0L)
         {
            jjmatchedKind = 42;
            jjmatchedPos = 3;
            return 2;
         }
         if ((active0 & 0x400000L) != 0L)
         {
            if (jjmatchedPos < 1)
            {
               jjmatchedKind = 42;
               jjmatchedPos = 1;
            }
            return -1;
         }
         return -1;
      case 4:
         if ((active0 & 0x1000000L) != 0L)
            return 2;
         if ((active0 & 0x10002bf0000L) != 0L)
         {
            jjmatchedKind = 42;
            jjmatchedPos = 4;
            return 2;
         }
         if ((active0 & 0x400000L) != 0L)
         {
            if (jjmatchedPos < 1)
            {
               jjmatchedKind = 42;
               jjmatchedPos = 1;
            }
            return -1;
         }
         return -1;
      case 5:
         if ((active0 & 0x10002bf0000L) != 0L)
         {
            jjmatchedKind = 42;
            jjmatchedPos = 5;
            return 2;
         }
         if ((active0 & 0x400000L) != 0L)
         {
            if (jjmatchedPos < 1)
            {
               jjmatchedKind = 42;
               jjmatchedPos = 1;
            }
            return -1;
         }
         return -1;
      case 6:
         if ((active0 & 0x10000800000L) != 0L)
            return 2;
         if ((active0 & 0x23f0000L) != 0L)
         {
            jjmatchedKind = 42;
            jjmatchedPos = 6;
            return 2;
         }
         if ((active0 & 0x400000L) != 0L)
         {
            if (jjmatchedPos < 1)
            {
               jjmatchedKind = 42;
               jjmatchedPos = 1;
            }
            return -1;
         }
         return -1;
      case 7:
         if ((active0 & 0x23f0000L) != 0L)
         {
            jjmatchedKind = 42;
            jjmatchedPos = 7;
            return 2;
         }
         if ((active0 & 0x400000L) != 0L)
         {
            if (jjmatchedPos < 1)
            {
               jjmatchedKind = 42;
               jjmatchedPos = 1;
            }
            return -1;
         }
         return -1;
      case 8:
         if ((active0 & 0x2000000L) != 0L)
            return 2;
         if ((active0 & 0x3f0000L) != 0L)
         {
            jjmatchedKind = 42;
            jjmatchedPos = 8;
            return 2;
         }
         return -1;
      case 9:
         if ((active0 & 0x3f0000L) != 0L)
         {
            jjmatchedKind = 42;
            jjmatchedPos = 9;
            return 2;
         }
         return -1;
      case 10:
         if ((active0 & 0xc0000L) != 0L)
            return 2;
         if ((active0 & 0x330000L) != 0L)
         {
            jjmatchedKind = 42;
            jjmatchedPos = 10;
            return 2;
         }
         return -1;
      case 11:
         if ((active0 & 0x120000L) != 0L)
         {
            jjmatchedKind = 42;
            jjmatchedPos = 11;
            return 2;
         }
         if ((active0 & 0x210000L) != 0L)
            return 2;
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0){
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_0(){
   switch(curChar)
   {
      case 33:
         return jjStopAtPos(0, 36);
      case 34:
         return jjStopAtPos(0, 7);
      case 40:
         return jjStopAtPos(0, 27);
      case 41:
         return jjStopAtPos(0, 28);
      case 42:
         return jjStopAtPos(0, 34);
      case 43:
         return jjStopAtPos(0, 32);
      case 45:
         return jjStopAtPos(0, 33);
      case 46:
         return jjStopAtPos(0, 38);
      case 58:
         return jjStopAtPos(0, 31);
      case 59:
         return jjStopAtPos(0, 37);
      case 60:
         return jjStopAtPos(0, 35);
      case 61:
         return jjStopAtPos(0, 29);
      case 63:
         return jjStopAtPos(0, 30);
      case 97:
         return jjMoveStringLiteralDfa1_0(0x4000L);
      case 98:
         return jjMoveStringLiteralDfa1_0(0x10000020000L);
      case 100:
         return jjMoveStringLiteralDfa1_0(0x800000L);
      case 102:
         return jjMoveStringLiteralDfa1_0(0x310000L);
      case 105:
         return jjMoveStringLiteralDfa1_0(0x8002402000L);
      case 108:
         return jjMoveStringLiteralDfa1_0(0x1000L);
      case 111:
         return jjMoveStringLiteralDfa1_0(0x4000000L);
      case 112:
         return jjMoveStringLiteralDfa1_0(0x8000L);
      case 114:
         return jjMoveStringLiteralDfa1_0(0xc0000L);
      case 117:
         return jjMoveStringLiteralDfa1_0(0x1000000L);
      default :
         return jjMoveNfa_0(1, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0){
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x20000L);
      case 100:
         return jjMoveStringLiteralDfa2_0(active0, 0x8000L);
      case 101:
         return jjMoveStringLiteralDfa2_0(active0, 0x8c1000L);
      case 110:
         if ((active0 & 0x2000L) != 0L)
         {
            jjmatchedKind = 13;
            jjmatchedPos = 1;
         }
         return jjMoveStringLiteralDfa2_0(active0, 0x8003004000L);
      case 111:
         return jjMoveStringLiteralDfa2_0(active0, 0x10000310000L);
      case 114:
         if ((active0 & 0x4000000L) != 0L)
            return jjStartNfaWithStates_0(1, 26, 2);
         break;
      case 115:
         return jjMoveStringLiteralDfa2_0(active0, 0x400000L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 32:
         return jjMoveStringLiteralDfa3_0(active0, 0x400000L);
      case 99:
         return jjMoveStringLiteralDfa3_0(active0, 0x20000L);
      case 100:
         if ((active0 & 0x4000L) != 0L)
            return jjStartNfaWithStates_0(2, 14, 2);
         break;
      case 102:
         return jjMoveStringLiteralDfa3_0(active0, 0x800000L);
      case 103:
         if ((active0 & 0x8000L) != 0L)
            return jjStartNfaWithStates_0(2, 15, 2);
         break;
      case 105:
         return jjMoveStringLiteralDfa3_0(active0, 0x1000000L);
      case 109:
         return jjMoveStringLiteralDfa3_0(active0, 0xc0000L);
      case 111:
         return jjMoveStringLiteralDfa3_0(active0, 0x10000000000L);
      case 114:
         return jjMoveStringLiteralDfa3_0(active0, 0x310000L);
      case 116:
         if ((active0 & 0x1000L) != 0L)
            return jjStartNfaWithStates_0(2, 12, 2);
         else if ((active0 & 0x8000000000L) != 0L)
         {
            jjmatchedKind = 39;
            jjmatchedPos = 2;
         }
         return jjMoveStringLiteralDfa3_0(active0, 0x2000000L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private int jjMoveStringLiteralDfa3_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 69:
         return jjMoveStringLiteralDfa4_0(active0, 0x100000L);
      case 80:
         return jjMoveStringLiteralDfa4_0(active0, 0x200000L);
      case 97:
         return jjMoveStringLiteralDfa4_0(active0, 0x800000L);
      case 101:
         return jjMoveStringLiteralDfa4_0(active0, 0x2400000L);
      case 107:
         return jjMoveStringLiteralDfa4_0(active0, 0x20000L);
      case 108:
         return jjMoveStringLiteralDfa4_0(active0, 0x10000000000L);
      case 111:
         return jjMoveStringLiteralDfa4_0(active0, 0x10c0000L);
      case 119:
         return jjMoveStringLiteralDfa4_0(active0, 0x10000L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private int jjMoveStringLiteralDfa4_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa5_0(active0, 0x10000L);
      case 101:
         return jjMoveStringLiteralDfa5_0(active0, 0x10000000000L);
      case 109:
         return jjMoveStringLiteralDfa5_0(active0, 0x400000L);
      case 110:
         if ((active0 & 0x1000000L) != 0L)
            return jjStartNfaWithStates_0(4, 24, 2);
         break;
      case 114:
         return jjMoveStringLiteralDfa5_0(active0, 0x2200000L);
      case 117:
         return jjMoveStringLiteralDfa5_0(active0, 0x800000L);
      case 118:
         return jjMoveStringLiteralDfa5_0(active0, 0xc0000L);
      case 119:
         return jjMoveStringLiteralDfa5_0(active0, 0x20000L);
      case 120:
         return jjMoveStringLiteralDfa5_0(active0, 0x100000L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
private int jjMoveStringLiteralDfa5_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa6_0(active0, 0x10000020000L);
      case 101:
         return jjMoveStringLiteralDfa6_0(active0, 0xc0000L);
      case 108:
         return jjMoveStringLiteralDfa6_0(active0, 0x800000L);
      case 111:
         return jjMoveStringLiteralDfa6_0(active0, 0x200000L);
      case 112:
         return jjMoveStringLiteralDfa6_0(active0, 0x500000L);
      case 114:
         return jjMoveStringLiteralDfa6_0(active0, 0x10000L);
      case 115:
         return jjMoveStringLiteralDfa6_0(active0, 0x2000000L);
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
private int jjMoveStringLiteralDfa6_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(4, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(5, active0);
      return 6;
   }
   switch(curChar)
   {
      case 69:
         return jjMoveStringLiteralDfa7_0(active0, 0x80000L);
      case 78:
         return jjMoveStringLiteralDfa7_0(active0, 0x40000L);
      case 99:
         return jjMoveStringLiteralDfa7_0(active0, 0x200000L);
      case 100:
         return jjMoveStringLiteralDfa7_0(active0, 0x10000L);
      case 101:
         return jjMoveStringLiteralDfa7_0(active0, 0x2000000L);
      case 110:
         if ((active0 & 0x10000000000L) != 0L)
            return jjStartNfaWithStates_0(6, 40, 2);
         break;
      case 114:
         return jjMoveStringLiteralDfa7_0(active0, 0x120000L);
      case 116:
         if ((active0 & 0x800000L) != 0L)
            return jjStartNfaWithStates_0(6, 23, 2);
         return jjMoveStringLiteralDfa7_0(active0, 0x400000L);
      default :
         break;
   }
   return jjStartNfa_0(5, active0);
}
private int jjMoveStringLiteralDfa7_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(5, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(6, active0);
      return 7;
   }
   switch(curChar)
   {
      case 83:
         return jjMoveStringLiteralDfa8_0(active0, 0x10000L);
      case 99:
         return jjMoveStringLiteralDfa8_0(active0, 0x2000000L);
      case 100:
         return jjMoveStringLiteralDfa8_0(active0, 0xa0000L);
      case 101:
         return jjMoveStringLiteralDfa8_0(active0, 0x300000L);
      case 111:
         return jjMoveStringLiteralDfa8_0(active0, 0x40000L);
      case 121:
         if ((active0 & 0x400000L) != 0L)
            return jjStopAtPos(7, 22);
         break;
      default :
         break;
   }
   return jjStartNfa_0(6, active0);
}
private int jjMoveStringLiteralDfa8_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(6, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(7, active0);
      return 8;
   }
   switch(curChar)
   {
      case 83:
         return jjMoveStringLiteralDfa9_0(active0, 0x20000L);
      case 100:
         return jjMoveStringLiteralDfa9_0(active0, 0x240000L);
      case 103:
         return jjMoveStringLiteralDfa9_0(active0, 0x80000L);
      case 108:
         return jjMoveStringLiteralDfa9_0(active0, 0x10000L);
      case 115:
         return jjMoveStringLiteralDfa9_0(active0, 0x100000L);
      case 116:
         if ((active0 & 0x2000000L) != 0L)
            return jjStartNfaWithStates_0(8, 25, 2);
         break;
      default :
         break;
   }
   return jjStartNfa_0(7, active0);
}
private int jjMoveStringLiteralDfa9_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(7, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(8, active0);
      return 9;
   }
   switch(curChar)
   {
      case 101:
         return jjMoveStringLiteralDfa10_0(active0, 0xc0000L);
      case 105:
         return jjMoveStringLiteralDfa10_0(active0, 0x10000L);
      case 108:
         return jjMoveStringLiteralDfa10_0(active0, 0x20000L);
      case 115:
         return jjMoveStringLiteralDfa10_0(active0, 0x100000L);
      case 117:
         return jjMoveStringLiteralDfa10_0(active0, 0x200000L);
      default :
         break;
   }
   return jjStartNfa_0(8, active0);
}
private int jjMoveStringLiteralDfa10_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(8, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(9, active0);
      return 10;
   }
   switch(curChar)
   {
      case 99:
         return jjMoveStringLiteralDfa11_0(active0, 0x10000L);
      case 105:
         return jjMoveStringLiteralDfa11_0(active0, 0x120000L);
      case 114:
         return jjMoveStringLiteralDfa11_0(active0, 0x200000L);
      case 115:
         if ((active0 & 0x40000L) != 0L)
            return jjStartNfaWithStates_0(10, 18, 2);
         else if ((active0 & 0x80000L) != 0L)
            return jjStartNfaWithStates_0(10, 19, 2);
         break;
      default :
         break;
   }
   return jjStartNfa_0(9, active0);
}
private int jjMoveStringLiteralDfa11_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(9, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(10, active0);
      return 11;
   }
   switch(curChar)
   {
      case 99:
         return jjMoveStringLiteralDfa12_0(active0, 0x20000L);
      case 101:
         if ((active0 & 0x10000L) != 0L)
            return jjStartNfaWithStates_0(11, 16, 2);
         else if ((active0 & 0x200000L) != 0L)
            return jjStartNfaWithStates_0(11, 21, 2);
         break;
      case 111:
         return jjMoveStringLiteralDfa12_0(active0, 0x100000L);
      default :
         break;
   }
   return jjStartNfa_0(10, active0);
}
private int jjMoveStringLiteralDfa12_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(10, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(11, active0);
      return 12;
   }
   switch(curChar)
   {
      case 101:
         if ((active0 & 0x20000L) != 0L)
            return jjStartNfaWithStates_0(12, 17, 2);
         break;
      case 110:
         if ((active0 & 0x100000L) != 0L)
            return jjStartNfaWithStates_0(12, 20, 2);
         break;
      default :
         break;
   }
   return jjStartNfa_0(11, active0);
}
private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 12;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 1:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 41)
                        kind = 41;
                     { jjCheckNAdd(0); }
                  }
                  else if (curChar == 47)
                     { jjAddStates(0, 1); }
                  break;
               case 0:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 41)
                     kind = 41;
                  { jjCheckNAdd(0); }
                  break;
               case 2:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 42)
                     kind = 42;
                  jjstateSet[jjnewStateCnt++] = 2;
                  break;
               case 3:
                  if (curChar == 47)
                     { jjAddStates(0, 1); }
                  break;
               case 4:
                  if (curChar == 42)
                     { jjCheckNAddTwoStates(5, 6); }
                  break;
               case 5:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     { jjCheckNAddTwoStates(5, 6); }
                  break;
               case 6:
                  if (curChar == 42)
                     { jjCheckNAddStates(2, 4); }
                  break;
               case 7:
                  if ((0xffff7bffffffffffL & l) != 0L)
                     { jjCheckNAddTwoStates(8, 6); }
                  break;
               case 8:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     { jjCheckNAddTwoStates(8, 6); }
                  break;
               case 9:
                  if (curChar == 47 && kind > 5)
                     kind = 5;
                  break;
               case 10:
                  if (curChar != 47)
                     break;
                  if (kind > 6)
                     kind = 6;
                  { jjCheckNAdd(11); }
                  break;
               case 11:
                  if ((0xfffffffffffffbffL & l) == 0L)
                     break;
                  if (kind > 6)
                     kind = 6;
                  { jjCheckNAdd(11); }
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 1:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 42)
                     kind = 42;
                  { jjCheckNAdd(2); }
                  break;
               case 2:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 42)
                     kind = 42;
                  { jjCheckNAdd(2); }
                  break;
               case 5:
                  { jjCheckNAddTwoStates(5, 6); }
                  break;
               case 7:
               case 8:
                  { jjCheckNAddTwoStates(8, 6); }
                  break;
               case 11:
                  if (kind > 6)
                     kind = 6;
                  jjstateSet[jjnewStateCnt++] = 11;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 5:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     { jjCheckNAddTwoStates(5, 6); }
                  break;
               case 7:
               case 8:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     { jjCheckNAddTwoStates(8, 6); }
                  break;
               case 11:
                  if ((jjbitVec0[i2] & l2) == 0L)
                     break;
                  if (kind > 6)
                     kind = 6;
                  jjstateSet[jjnewStateCnt++] = 11;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 12 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
private int jjMoveStringLiteralDfa0_2()
{
   return jjMoveNfa_2(0, 0);
}
private int jjMoveNfa_2(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 1;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x800400000000L & l) != 0L)
                     kind = 11;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x14404410000000L & l) != 0L)
                     kind = 11;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 1 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
private final int jjStopStringLiteralDfa_1(int pos, long active0){
   switch (pos)
   {
      default :
         return -1;
   }
}
private final int jjStartNfa_1(int pos, long active0){
   return jjMoveNfa_1(jjStopStringLiteralDfa_1(pos, active0), pos + 1);
}
private int jjMoveStringLiteralDfa0_1(){
   switch(curChar)
   {
      case 92:
         return jjStopAtPos(0, 8);
      default :
         return jjMoveNfa_1(0, 0);
   }
}
private int jjMoveNfa_1(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 2;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0xfffffffbffffffffL & l) != 0L)
                  {
                     if (kind > 10)
                        kind = 10;
                  }
                  else if (curChar == 34)
                  {
                     if (kind > 9)
                        kind = 9;
                  }
                  break;
               case 1:
                  if ((0xfffffffbffffffffL & l) != 0L)
                     kind = 10;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0xffffffffefffffffL & l) != 0L)
                     kind = 10;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((jjbitVec0[i2] & l2) != 0L && kind > 10)
                     kind = 10;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 2 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   4, 10, 6, 7, 9, 
};

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, "\42", null, null, null, null, 
"\154\145\164", "\151\156", "\141\156\144", "\160\144\147", 
"\146\157\162\167\141\162\144\123\154\151\143\145", "\142\141\143\153\167\141\162\144\123\154\151\143\145", 
"\162\145\155\157\166\145\116\157\144\145\163", "\162\145\155\157\166\145\105\144\147\145\163", 
"\146\157\162\105\170\160\162\145\163\163\151\157\156", "\146\157\162\120\162\157\143\145\144\165\162\145", 
"\151\163\40\145\155\160\164\171", "\144\145\146\141\165\154\164", "\165\156\151\157\156", 
"\151\156\164\145\162\163\145\143\164", "\157\162", "\50", "\51", "\75", "\77", "\72", "\53", "\55", "\52", "\74", 
"\41", "\73", "\56", "\151\156\164", "\142\157\157\154\145\141\156", null, null, null, 
null, };
protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      jjmatchedPos = -1;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   for (;;)
   {
     switch(curLexState)
     {
       case 0:
         try { input_stream.backup(0);
            while (curChar <= 32 && (0x100002600L & (1L << curChar)) != 0L)
               curChar = input_stream.BeginToken();
         }
         catch (java.io.IOException e1) { continue EOFLoop; }
         jjmatchedKind = 0x7fffffff;
         jjmatchedPos = 0;
         curPos = jjMoveStringLiteralDfa0_0();
         break;
       case 1:
         jjmatchedKind = 0x7fffffff;
         jjmatchedPos = 0;
         curPos = jjMoveStringLiteralDfa0_1();
         break;
       case 2:
         jjmatchedKind = 0x7fffffff;
         jjmatchedPos = 0;
         curPos = jjMoveStringLiteralDfa0_2();
         break;
     }
     if (jjmatchedKind != 0x7fffffff)
     {
        if (jjmatchedPos + 1 < curPos)
           input_stream.backup(curPos - jjmatchedPos - 1);
        if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
        {
           matchedToken = jjFillToken();
       if (jjnewLexState[jjmatchedKind] != -1)
         curLexState = jjnewLexState[jjmatchedKind];
           return matchedToken;
        }
        else if ((jjtoSkip[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
        {
         if (jjnewLexState[jjmatchedKind] != -1)
           curLexState = jjnewLexState[jjmatchedKind];
           continue EOFLoop;
        }
      if (jjnewLexState[jjmatchedKind] != -1)
        curLexState = jjnewLexState[jjmatchedKind];
        curPos = 0;
        jjmatchedKind = 0x7fffffff;
        try {
           curChar = input_stream.readChar();
           continue;
        }
        catch (java.io.IOException e1) { }
     }
     int error_line = input_stream.getEndLine();
     int error_column = input_stream.getEndColumn();
     String error_after = null;
     boolean EOFSeen = false;
     try { input_stream.readChar(); input_stream.backup(1); }
     catch (java.io.IOException e1) {
        EOFSeen = true;
        error_after = curPos <= 1 ? "" : input_stream.GetImage();
        if (curChar == '\n' || curChar == '\r') {
           error_line++;
           error_column = 0;
        }
        else
           error_column++;
     }
     if (!EOFSeen) {
        input_stream.backup(1);
        error_after = curPos <= 1 ? "" : input_stream.GetImage();
     }
     throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
   }
  }
}

private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

    /** Constructor. */
    public PidginParserTokenManager(SimpleCharStream stream){

      if (SimpleCharStream.staticFlag)
            throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");

    input_stream = stream;
  }

  /** Constructor. */
  public PidginParserTokenManager (SimpleCharStream stream, int lexState){
    ReInit(stream);
    SwitchTo(lexState);
  }

  /** Reinitialise parser. */
  public void ReInit(SimpleCharStream stream)
  {
    jjmatchedPos = jjnewStateCnt = 0;
    curLexState = defaultLexState;
    input_stream = stream;
    ReInitRounds();
  }

  private void ReInitRounds()
  {
    int i;
    jjround = 0x80000001;
    for (i = 12; i-- > 0;)
      jjrounds[i] = 0x80000000;
  }

  /** Reinitialise parser. */
  public void ReInit(SimpleCharStream stream, int lexState)
  {
    ReInit(stream);
    SwitchTo(lexState);
  }

  /** Switch to specified lex state. */
  public void SwitchTo(int lexState)
  {
    if (lexState >= 3 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
    else
      curLexState = lexState;
  }

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
   "STRING_STATE",
   "ESC_STATE",
};

/** Lex State array. */
public static final int[] jjnewLexState = {
   -1, -1, -1, -1, -1, -1, -1, 1, 2, 0, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
};
static final long[] jjtoToken = {
   0x7fffffffe81L, 
};
static final long[] jjtoSkip = {
   0x7eL, 
};
static final long[] jjtoMore = {
   0x100L, 
};
    protected SimpleCharStream  input_stream;

    private final int[] jjrounds = new int[12];
    private final int[] jjstateSet = new int[2 * 12];

    
    protected char curChar;
}
