package com.project.shopping.utils;

import java.util.function.Supplier;

public class UseCase {
	private UseCaseBuilder builder;
	
	private UseCase() {}
	private UseCase(UseCaseBuilder builder) {
		this.builder = builder;
	}
	
	public static class UseCaseBuilder {
		private int whenCount;
		private int thenCount;
		private int elseCount;
		private boolean bool;
		private Object data;

		public UseCaseBuilder caseWhen(boolean bool) {
			if(elseCount>0) {
				throw new RuntimeException("anymore can\'t use caseWhen.");
			}
			whenCount++;
			this.bool = bool;

			return this;
		}

		public UseCaseBuilder caseThen(Supplier<?> supplier) {
			thenCount++;
			if(whenCount!=thenCount) {
				throw new RuntimeException("do not used \'caseWhen\'.");
			}

			data = supplier.get();
			
			return this;
		}

		public UseCaseBuilder caseThen(Runnable runnable) {
			thenCount++;
			if(whenCount!=thenCount) {
				throw new RuntimeException("do not used \'caseWhen\'.");
			}
			if(this.bool) {
				runnable.run();
			}

			return this;
		}

		public UseCaseBuilder caseElse(Runnable runnable) {
			elseCount++;
			if(whenCount<elseCount) {
				throw new RuntimeException("do not used \'caseWhen\'.");
			}
			if(!this.bool) {
				runnable.run();
			}
			
			return this;
		}

		public UseCaseBuilder caseElse(Supplier<?> supplier) {
			elseCount++;
			if(whenCount<elseCount) {
				throw new RuntimeException("do not used \'caseWhen\'.");
			}
			if(!this.bool) {
				data = supplier.get();
			}

			return this;
		}

		public UseCase build() {
			return new UseCase(this);
		}
	}

	public static UseCaseBuilder builder() {
		return new UseCaseBuilder();
	}

	public boolean isTrue() {
		return builder.bool;
	}

	public Object get() {
		return builder.data;
	}


}
