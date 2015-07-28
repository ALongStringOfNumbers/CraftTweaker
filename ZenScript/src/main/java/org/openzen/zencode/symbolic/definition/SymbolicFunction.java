/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.definition;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openzen.zencode.parser.definition.ParsedFunction;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.member.definition.FieldMember;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.LocalSymbol;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.scope.MethodScope;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.symbols.ImportableSymbol;
import org.openzen.zencode.symbolic.type.TypeDefinition;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;

/**
 *
 * @author Stan
 * @param <E>
 */
public final class SymbolicFunction<E extends IPartialExpression<E>> extends AbstractSymbolicDefinition<E>
{
	private final ParsedFunction source;
	
	private final CodePosition position;
	private final String name;
	private Statement<E> content;
	
	private final IMethodScope<E> methodScope;
	private final Map<LocalSymbol<E>, Capture<E>> captured = new HashMap<>();

	public SymbolicFunction(CodePosition position, int modifiers, String name, MethodHeader<E> header, IModuleScope<E> scope)
	{
		super(modifiers, Collections.<SymbolicAnnotation<E>>emptyList(), scope, false, false);
		
		this.position = position;
		this.name = name;
		
		source = null;
		methodScope = new MethodScope<>(getScope(), header, false);
	}
	
	public SymbolicFunction(ParsedFunction source, IModuleScope<E> scope)
	{
		super(source, scope, false, false);
		
		this.source = source;
		position = source.getPosition();
		MethodHeader<E> header = source.getSignature().compile(getScope());
		methodScope = new MethodScope<>(getScope(), header, false);
		this.name = source.getName();
	}
	
	public final IMethodScope<E> getMethodScope()
	{
		return methodScope;
	}

	/*
	@Override
	public void compile()
	{
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		classWriter.visitSource(position.getFile().getFileName(), null);
		
		classWriter.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC, generatedClassName, null, internal(Object.class), null);
		MethodOutput methodOutput = new MethodOutput(classWriter, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "call", type.getMethodHeader().getSignature(), null, null);
		methodOutput.start();
		
		for (Statement<E, T> statement : statements) {
			statement.compile(methodOutput);
		}
		
		methodOutput.ret();
		methodOutput.end();
		
		classWriter.visitEnd();
		classScope.putClass(generatedClassName, classWriter.toByteArray());
	}*/

	public MethodHeader<E> getHeader()
	{
		return methodScope.getMethodHeader();
	}

	public IPartialExpression<E> addCapture(CodePosition position, IMethodScope<E> scope, LocalSymbol<E> local)
	{
		if (!captured.containsKey(local)) {
			FieldMember<E> field = new FieldMember<E>(
					getScope(),
					Modifier.PRIVATE.getCode() | Modifier.FINAL.getCode(),
					"__capture" + captured.size(),
					local.getType());
			captured.put(local, new Capture<E>(local, field));
		}

		return scope.getExpressionCompiler().getVirtualField(position, scope, scope.getThis(position, null), captured.get(local).field);
	}

	@Override
	public void collectInnerDefinitions(List<ISymbolicDefinition<E>> units, IModuleScope<E> scope)
	{
		
	}

	@Override
	public void compileMembers()
	{
		super.compileMembers();
	}

	@Override
	public void compileMemberContents()
	{
		super.compileMemberContents();
		
		if (source == null)
			return;
		
		content = source.getContents().compile(methodScope);
	}

	@Override
	public List<? extends ITypeVariable<E>> getTypeVariables()
	{
		return methodScope.getMethodHeader().getGenericParameters();
	}

	@Override
	public void register(IModuleScope<E> scope)
	{
		scope.putImport(name, new ImportableSymbol<>(new TypeDefinition<E>(getTypeVariables(), false, false)), position);
	}
	
	@Override
	public boolean isStruct()
	{
		return false;
	}
	
	private static class Capture<E extends IPartialExpression<E>>
	{
		private final LocalSymbol<E> local;
		private final FieldMember<E> field;

		public Capture(LocalSymbol<E> local, FieldMember<E> field)
		{
			this.local = local;
			this.field = field;
		}
	}
}